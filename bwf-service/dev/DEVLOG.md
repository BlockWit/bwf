# Developers log

## Создание пары
1. Deputer ожидает события создания пары в эфире 
2. После получения события Deputer создает SwapPairStateMachine и обновляет событие как TxLogPhase.ConfirmRequest 

## Как проходит своп ETH -> BSC
Перед проведелением свопа - единичная для всех свопов операция - создание пары.

1. Пользователь разрешает снять токены со своего контракта - increaseAllowance у ERC20
2. Пользователь вызывает в ETH у контракта свопа swapETH2BSC(address erc20Addr - адрес токена, uint256 amount - сколько свопим).
	 Контракт свопа:
	 - перемещает amount токенов с адреса пользователя на контракт. (в 1 пользователь разрешил это)
	 - отправляет ETH комиссию владельцу контракта. (Контракт позволяет устанавливать комиссию за своп, но она там должна быть равна - это проблема, нужно больше либо равно и возвращать сдачу)
	 - генерирует событие SwapStarted
3. Deputer слушает блокчейны и получает SwapStarted. Он записывает это событие в базу со статусом TxPhase = SeenRequest. (это отдельный обработчик)
	 - deputer формирует Swap с полями (это отдельный обработчик):
		 - Status:      - SwapTokenReceived - если прошли проверки (пара существует, формат адресов, amount соответсвует правилам пары - кол-во знаков и прочее), если нет то SwapQuoteRejected - https://github.com/binance-chain/bsc-eth-swap/blob/50fcca999745c289d1d5b55d7ad2662ef6b8f489/swap/swap.go#L182
		 - Sponsor:     - из SwapStarted поля fromAddr
		 - BEP20Addr:   bep20Addr.String(),
		 - ERC20Addr:   erc20Addr.String(),
		 - Symbol:      - заполняется из SwapPair поля Symbol из базы 
		 - Amount:      - из SwapStarted поля Amount
		 - Decimals:    - заполняется из SwapPair поля Decimals из базы
		 - Direction:   - заполняется сразу в зависимости от направления обмена
		 - StartTxHash: - заполняется из таблицы SwapStartedTxs из поля txHash
		 - FillTxHash:  - ""
		 - Log:         - "" - если без ошибок, иначе пишем сообщение
	- Добавляется getSwapHMAC (https://github.com/binance-chain/bsc-eth-swap/blob/50fcca999745c289d1d5b55d7ad2662ef6b8f489/swap/swap.go#L145) и пишет в базу
	- Одновременно обновляется лог SwapStartTx:
	   - Phase = ConfirmRequest
	   - update_time - текущее
	- (это уже отдельный обработчик ) - deputer - выбирает все SwapStartTx с Phase = ConfirmRequest
	- по полученным логам по полю txHash выбирает свопы из базы Swap
	- по каждому свопу - если статус swap.Status == SwapTokenReceived то ставим статус swap.Status = SwapConfirmed и обновляем HMAC ???
	- обновояем у соответсвующего свопу лога "phase"  - model.AckRequest и дату обновления
	- (другой обработчик) - выбираем свопы со статусами - SwapConfirmed || SwapSending
	- проверяем HMAC - чтобы в базе наверное никто не подменил ничего!!!
	- берем из базы своп пару SwapPair - если пары нет, то ставим свопу статус - swap.Status = SwapQuoteRejected и обновляем время и next
	- если статус у свопа = swap.Status == SwapSending - то берем модель из SwapFillTx и смотрим есть ли хэш FillSwapTxHash. Если "", то свопу статус SwapConfirmedm, иначе:
		- создаем FillSwapTx и ставим статус свопу swap.Status = SwapSent и поле FillTxHash заполняем и обновляем дату и HMAC и выходим
	- если статус у свопа = swap.Status != SwapSending то ставим статус SwapSending и обновляем дату и HMAC
	- делаем своп https://github.com/binance-chain/bsc-eth-swap/blob/50fcca999745c289d1d5b55d7ad2662ef6b8f489/swap/swap.go#L372
		- 

4. 
5. 

## Таблицы из нативного клиента

Взяты из https://github.com/binance-chain/bsc-eth-swap/blob/main/model/model.go#L54

### SwapPair

https://github.com/binance-chain/bsc-eth-swap/blob/main/model/swap_pair.go#L11

1. Sponsor    string
2. Symbol     string
3. Name       string
4. Decimals   int   
5. BEP20Addr  string
6. ERC20Addr  string
7. Available  bool  
8. LowBound   string
9. UpperBound string
10. IconUrl    string
		
### Swap

https://github.com/binance-chain/bsc-eth-swap/blob/main/model/swap.go#L101

1. Status common.SwapStatus 
2. Sponsor string - // the user addreess who start this swap
3. BEP20Addr string 
4. ERC20Addr string
5. Symbol    string
6. Amount    string
7. Decimals  int   
8. Direction common.SwapDirection
9. StartTxHash string 	// The tx hash confirmed deposit
10.	FillTxHash string // The tx hash confirmed withdraw
11.	Log string 	// used to log more message about how this swap failed or invalid
12.	RecordHash string 

### SwapStartTxLog - таблица, для хранения транзакций начала свопа.
Универсальная для обоих чейнов (по полю Chain определяется)
1. Id    int64
2. Chain string
3. TokenAddr   string
4. FromAddress string
5. Amount      string
6. FeeAmount   string
7. Status      TxStatus - имеет два сосоттяния - confirmed и init. 
В нативном клиенте используется для избежания форка. 
После того как пройдет n-ое кол-во блоков транзакция ставится в состояние confirmed.
В нашей реализации поля нет.
8. TxHash      string
9. BlockHash    string
10. Height       int64
11. ConfirmedNum int64  - кол-во блоков, прошедших с момента получения транзакции.
В нативном клиенте используется для избежания форка.
После того как пройдет n-ое кол-во блоков транзакция ставится в состояние confirmed.
В нашей реализации поля нет.
12. Phase TxPhase
13. UpdateTime int64
14. CreateTime int64
		
### BlockLog
### SwapPairCreatTx
### SwapPairRegisterTxLog
### SwapPairStateMachine

1. Status common.SwapPairStatus
2. ERC20Addr string
3. BEP20Addr string
4. Sponsor  string
5. Symbol   string 
6. Name     string 
7. Decimals int
8. PairRegisterTxHash string 
9. PairCreatTxHash    string
10 Log string
11.	RecordHash string

### RetrySwap
### RetrySwapTx


# Текущий флов
1. Регистрируем пару ETH
2. Создаем пару в BSC
3. Создаем пару в BWF

Пока только одна таблица должна быть

Пользователь создает запрос на SWAP
- ищем событие
- создаем транзакцию
- мониторим событие
