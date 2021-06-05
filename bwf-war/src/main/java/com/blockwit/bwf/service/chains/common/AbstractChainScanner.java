package com.blockwit.bwf.service.chains.common;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.chain.*;
import com.blockwit.bwf.model.chain.factories.*;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.model.chain.fields.ITxLog;
import com.blockwit.bwf.model.tasks.SystemSchedTasks;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.SchedTasksService;
import com.blockwit.bwf.service.SwapPairService;
import com.blockwit.bwf.service.SwapService;
import com.blockwit.bwf.service.chains.common.utils.*;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractChainScanner {

  private OptionService optionService;
  private IChainServicePool chainServicePool;
  private IChainServicePool oppositeChainServicePool;
  private SwapService swapService;
  private Chains chain;
  private ChainHelper chainHelper;
  private SwapPairService swapPairService;
  private SchedTasksService schedTasksService;

  private SwapDirection swapDir;

  private Chains oppositeChain;

  private AtomicBoolean destroy = new AtomicBoolean(false);
  private AtomicBoolean destroyFinished = new AtomicBoolean(false);

  private boolean skipStartBlocks = true;

  public AbstractChainScanner(Chains chain,
                              ChainHelper chainHelper,
                              IChainServicePool chainServicePool,
                              IChainServicePool oppositeChainServicePool,
                              OptionService optionService,
                              SwapService swapService,
                              SwapPairService swapPairService,
                              SchedTasksService schedTasksService) {
    this.chain = chain;
    this.oppositeChain = chain.equals(Chains.Ethereum) ? Chains.BinanceSmartChain : Chains.Ethereum;
    this.swapPairService = swapPairService;
    this.chainHelper = chainHelper;
    this.chainServicePool = chainServicePool;
    this.oppositeChainServicePool = oppositeChainServicePool;
    this.optionService = optionService;
    this.swapService = swapService;
    this.swapDir = chain.equals(Chains.Ethereum) ? SwapDirection.ETH_TO_BSC : SwapDirection.BSC_TO_ETH;

    this.schedTasksService = schedTasksService;
  }

  @PreDestroy
  public void waitDestroy() {
    log.info("Waiting for " + chain + " chain processor destroy");
    destroy.set(true);
    while (!destroyFinished.get()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    log.info(chain + " chain processor destroy process finished");
  }

  @Scheduled(fixedDelay = 1000)
  protected void chainLogsScanner() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        chain,
        SystemSchedTasks.TASK_ID_LOGS_SCANNER,
        chainServicePool,
        oppositeChainServicePool, (chainService, oppositeChainService) ->

            WithOptionsProfiled.process(optionService,
                List.of(chain + OptionService.OPTION_Chain_SWAPPER_ADDR,
                    chain + OptionService.OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ,
                    chain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT,
                    chain + OptionService.OPTION_Chain_PASS_START_BLOCKS),
                options -> {

                  IChainAddress swapContractAddr = (IChainAddress) options.get(chain + OptionService.OPTION_Chain_SWAPPER_ADDR).getPerformedValue();
                  Long blocksRangePerIter = (Long) options.get(chain + OptionService.OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ).getPerformedValue();
                  Long forkBlocksCountProtection = (Long) options.get(chain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT).getPerformedValue();
                  Long passStartBlocks = (Long) options.get(chain + OptionService.OPTION_Chain_PASS_START_BLOCKS).getPerformedValue();

                  while (WithOptional.processIsRepeat(chainService.getHeight(),
                      chainHeight -> WithOptionsProfiled.processIsRepeat(optionService, chain + OptionService.OPTION_Chain_CURRENT_BLOCK,
                          currentBlockNumberOption ->

                              PerformSchedTask.onlyRunning(destroy,
                                  destroyFinished,
                                  schedTasksService,
                                  chain,
                                  SystemSchedTasks.TASK_ID_LOGS_SCANNER, () -> {
                                    long lastPerformedBlockNumber = (Long) currentBlockNumberOption.getPerformedValue();
                                    long currentBlockNumber = lastPerformedBlockNumber == 0 ? 0 : lastPerformedBlockNumber + 1;

                                    return WithOptionsProfiled.processIsRepeat(optionService, chain + OptionService.OPTION_Chain_START_BLOCK,
                                        startBlockOption -> {

                                          if (skipStartBlocks == true) {
                                            Long serverStartBlock = (Long) startBlockOption.getPerformedValue();
                                            if (serverStartBlock + passStartBlocks >= chainHeight) {
                                              log.info("Prevent logs scanning while " + passStartBlocks + " start blocks not passed.");
                                              return false;
                                            } else {
                                              skipStartBlocks = false;
                                            }
                                          }

                                          long chainScanHeightLimit = chainHeight - forkBlocksCountProtection;
                                          if (chainScanHeightLimit > 0 && currentBlockNumber <= chainScanHeightLimit) {

                                            long unlimitedEndBlockNumber = currentBlockNumber + blocksRangePerIter;
                                            long endBlockNumber = unlimitedEndBlockNumber > chainScanHeightLimit ? chainScanHeightLimit : unlimitedEndBlockNumber;

                                            log.trace("Scan blocks from " + currentBlockNumber + " to " + endBlockNumber);
                                            // =========== start perform blocks information ============//

                                            // TODO: You can check forks by block hash and parent block hash as in binance original code
                                            // 1. get logs
                                            return chainService.getEvents(swapContractAddr, currentBlockNumber, endBlockNumber).fold(
                                                error -> {
                                                  log.error("Error during getting events - " + error.getDescr());
                                                  return false;
                                                },
                                                events -> {

                                                  // 2. Convert to blockchain-independent logs format for
                                                  List<ITxLog> swapLogs = chainHelper.convertToTxLogs(events);

                                                  // 3. Save logs to DB and update blocks
                                                  swapService.saveSwapStartLogsIfNotExistsAndUpdateOptions(swapLogs,
                                                      currentBlockNumberOption.toBuilder().value(endBlockNumber + "").build(),
                                                      startBlockOption.toBuilder().value(endBlockNumber + "").build());

                                                  return true;
                                                });
                                          } else
                                            return false;
                                        });

                                  })

                      ))) ;

                }));
  }

  /**
   * Собирает транзакции по логам
   */
  @Scheduled(fixedDelay = 1000)
  protected void chainTransactionsUpdate() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        chain,
        SystemSchedTasks.TASK_ID_TX_UPDATER,
        chainServicePool,
        oppositeChainServicePool, (chainService, oppositeChainService) -> {

          // Ищем транзакции для логов
          WhileExists.process(() -> swapService.findTxLogs(chain, TxLogStatus.WAIT_STORE_TX), logs ->
              logs.forEach(log -> {
                Either<Error, Optional<Transaction>> eitherTx = chainService.getTx(log.getTxHash());
                if (eitherTx.isLeft()) {
                  eitherTx.mapLeft(error -> swapService.updateTxLog(log.updateStatusLog(TxLogStatus.STORE_TX_ERROR, error.getDescr())));
                } else {
                  eitherTx.forEach(txOpt -> txOpt.ifPresentOrElse(
                      tx -> swapService.updateTxLogAndCreateTx(log.updateStatus(TxLogStatus.INIT), Tx.builder()
                          .chain(chain)
                          .fromAddr(ChainAddressFactory.create(tx.getFrom()))
                          .toAddr(ChainAddressFactory.create(tx.getTo()))
                          .amount(ChainNumberFactory.create(tx.getValue().toString()))
                          .gas(ChainNumberFactory.create(tx.getGas().toString()))
                          .gasPrice(ChainNumberFactory.create(tx.getGasPrice().toString()))
                          .txHash(TxHashFactory.create(tx.getHash()))
                          .blockHash(BlockHashFactory.create(tx.getBlockHash()))
                          .height(BlockNumberFactory.create(tx.getBlockNumber().longValue()))
                          .nonce(tx.getNonce().longValue())
                          .status(TxStatus.INIT)
                          .createTime(System.currentTimeMillis())
                          .build()),
                      () -> swapService.updateTxLog(log.updateStatusLog(TxLogStatus.STORE_TX_ERROR, "Can't find transaction"))
                  ));
                }
              }));

          // Ставим логи с отсутствующими транзакциями на повторный круг - в будущем дифференцируем поведение по ошибкам
          WhileExists.process(() -> swapService.findTxLogs(chain, TxLogStatus.STORE_TX_ERROR), logs ->
              swapService.updateTxLogs(StreamEx.of(logs).map(log -> log.updateStatus(TxLogStatus.WAIT_STORE_TX)).toList())
          );

        });
  }


  @Scheduled(fixedDelay = 1000)
  protected void swapLogsSynchronizer() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        chain,
        SystemSchedTasks.TASK_ID_SWAP_LOGS_SYNC,
        chainServicePool,
        oppositeChainServicePool, (chainService, oppositeChainService) -> {

          /**
           * События финализации и старта могут попадать в базу в разное время. Потому как сканер для разны чейнов
           * работает с разной скоростью и начинает с разных блоков.
           *
           * Поэтому событие инализации может попасть в БЧ раньше чем событие старта. В этом случае мы сначала
           * помечаем своп как OLD. Но нам нужно потом провреить, не пришла ли стартовая транзакция в состоянии INIT,
           * которая соответсвует OLD свопам. Если так, то мы OLD своп ставим как finished.
           *
           * TODO: Проверять по времени противоположные блоки !!!
           *
           */
          WhileExists.process(() -> swapService.findSwapStartTxLogs(chain, TxLogStatus.INIT), swapStartTxLogs -> {

            List<Swap> oldSwaps = swapService.findSwapsByStartTxHashAndStatus(
                StreamEx.of(swapStartTxLogs).map(SwapStartTxLog::getTxHash).toList(),
                SwapStatus.SWAP_OLD);

            Set<String> swapStartHashesFromOldSwaps = StreamEx.of(oldSwaps)
                .map(Swap::getStartTxHash)
                .map(ITxHash::getHash)
                .toSet();

            swapService.updateSwapsAndStartLogs(
                StreamEx.of(oldSwaps)
                    .map(s -> s.update(SwapStatus.SWAP_FINISHED))
                    .toList(),
                StreamEx.of(swapStartTxLogs)
                    .filter(t -> swapStartHashesFromOldSwaps.contains(t.getTxHash().getHash()))
                    .map(t -> t.updateStatus(TxLogStatus.PROCESSED)).toList());

            swapService.updateStartTxLogs(
                StreamEx.of(swapStartTxLogs)
                    .filter(t -> !swapStartHashesFromOldSwaps.contains(t.getTxHash().getHash()))
                    .map(t -> t.updateStatus(TxLogStatus.RESTORE_INIT)).toList());

          });

          WhileExists.process(() -> swapService.findSwapStartTxLogs(chain, TxLogStatus.RESTORE_INIT), swapStartTxLogs ->
              swapService.updateStartTxLogs(StreamEx.of(swapStartTxLogs).map(t -> t.updateStatus(TxLogStatus.INIT)).toList())
          );


          /**
           *
           * Ищем INIT транзакции текущего чейна и создаем SWAP инстансы в базе
           * И одновременно помечаем что транзакция просмотрена, т.е. PROCESSED
           *
           */
          WhileExists.process(() -> swapService.findSwapStartTxLogs(chain, TxLogStatus.INIT), swapStartTxLogs ->
              // TODO: Делаем проверки, и, если что reject
              swapService.createSwapsAndUpdateStartTxLogs(
                  StreamEx.of(swapStartTxLogs).map(t -> t.updateStatus(TxLogStatus.PROCESSED)).toList(),
                  StreamEx.of(swapStartTxLogs).map(t -> Swap.builder()
                      .status(SwapStatus.SWAP_START_EVENT_RECEIVED)
                      .address(t.getFromAddr())
                      .amount(t.getAmount())
                      .direction(swapDir)
                      .startTxHash(t.getTxHash())
                      .createTime(System.currentTimeMillis())
                      .build()).toList()));

          /**
           *
           * По событиям финализации определяем что своп был осуществлен
           * TODO: Если не находим по финализированному хэшу своп,
           * то это значит что либо не верный хэш был в транзакции, либо сбой в сканере,
           * Ранее в обработчике долден был бюыть создан своп если есть startTx транзакция.
           * Если нет свопа, значит не было Tx старт транзакциии. Вохможно она появилась
           * раньше чем у нас стартовый блок! Тогда создаем своп как OLD
           *
           * 1. Берет все финализированные логи
           * 2. Ищет по каждой своп
           * 3. Обновляет своп и финализированный лог
           *
           */
          // TODO :
          WhileExists.process(() -> swapService.findSwapFinalizedTxLogs(oppositeChain, TxLogStatus.INIT),
              txLogs -> txLogs.forEach(txLog -> {
                    swapService.findSwapByStartTxHash(txLog.getCreateTxHash()).ifPresentOrElse(swap ->
                            swapService.updateSwapAndFinalizeLog(
                                swap.updateFinalized(SwapStatus.SWAP_FINISHED, txLog.getTxHash()),
                                txLog.updateStatus(TxLogStatus.PROCESSED))
                        , () -> {
                          log.warn("Can't find swap for finalize tx log with id " + txLog.getId() +
                              ". It can be possible in two cases: \n1. If opposite chain sync process outrun target chain. " +
                              "In this case after syncing target blockchain swap will update. \n2. If target chain start scan " +
                              "block greater than block with start tx log. In this case swap remain status OLD.");

                          swapService.updateSwapAndFinalizeLog(
                              Swap.builder()
                                  .status(SwapStatus.SWAP_OLD)
                                  .address(txLog.getToAddr())
                                  .amount(txLog.getAmount())
                                  .direction(swapDir)
                                  .finalizeTxHash(txLog.getTxHash())
                                  .startTxHash(txLog.getCreateTxHash())
                                  .createTime(System.currentTimeMillis())
                                  .build(),
                              txLog.updateStatus(TxLogStatus.PROCESSED));

                        });
                  }
              ));

        });
  }

  @Scheduled(fixedDelay = 5000)
  protected void swapsFixer() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        chain,
        SystemSchedTasks.TASK_ID_SWAP_FIXER,
        chainServicePool,
        oppositeChainServicePool, (chainService, oppositeChainService) -> {

          /**
           *
           * TODO: Ситуация, когда у гас waiting_finalize есть с finalized tx в статусе processed
           *
           */
          List<Swap> swaps = swapService.findSwaps(SwapStatus.SWAP_OPPOSITE_CHAIN_WAITING_FINALIZE_EVENT, swapDir);

          List<Swap> swapsToUpdate = new ArrayList<>();
          List<SwapFinalizeTxLog> finLogs = swapService.findSwapFinalizedTxLogsByStartHashes(StreamEx.of(swaps)
              .map(t -> t.getStartTxHash()).toList(), TxLogStatus.PROCESSED);
          swaps.forEach(swap ->
              StreamEx.of(finLogs).findAny(t -> t.getCreateTxHash().equals(swap.getStartTxHash())).ifPresentOrElse(finLog ->
                      swapsToUpdate.add(swap.update(SwapStatus.SWAP_FINISHED))
                  , () -> {
                    //String error = "Can't find finalize log for swap with start hash " + swap.getStartTxHash().getHash();
                    //swapService.updateSwap(swap.updateError(SwapStatus.SWAP_OPPOSITE_CHAIN_WAITING_FINALIZE_EVENT, error));
                    //log.error(error);

                  }
              ));
          swapService.updateSwaps(swapsToUpdate);


        });
  }


  @Scheduled(fixedDelay = 1000)
  protected void chainSwapProcessor() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        chain,
        SystemSchedTasks.TASK_ID_SWAP_PROCESS,
        chainServicePool,
        oppositeChainServicePool, (chainService, oppositeChainService) ->

            WithOptionsProfiled.process(optionService,
                List.of(chain + OptionService.OPTION_Chain_CURRENT_BLOCK,
                    chain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT,
                    oppositeChain + OptionService.OPTION_Chain_CURRENT_BLOCK,
                    oppositeChain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT),
                options -> {

                  Long currentBlockNumber = (Long) options.get(chain + OptionService.OPTION_Chain_CURRENT_BLOCK).getPerformedValue();
                  Long forkBlocksCountProtection = (Long) options.get(chain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT).getPerformedValue();
                  Long oppositeCurrentBlockNumber = (Long) options.get(oppositeChain + OptionService.OPTION_Chain_CURRENT_BLOCK).getPerformedValue();
                  Long oppositeForkBlocksCountProtection = (Long) options.get(oppositeChain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT).getPerformedValue();

                  // Оба блокчейна обязательно должны быть синхронизированы
                  WithOptional.process(chainService.getHeight(), chainHeight -> {
                    long chainScanHeightLimit = chainHeight - forkBlocksCountProtection;
                    if (chainScanHeightLimit > 0 && currentBlockNumber == chainScanHeightLimit) {
                      WithOptional.process(oppositeChainService.getHeight(), oppositeChainHeight -> {
                        long oppositeChainScanHeightLimit = oppositeChainHeight - oppositeForkBlocksCountProtection;
                        if (oppositeChainScanHeightLimit > 0 && oppositeCurrentBlockNumber == oppositeChainScanHeightLimit) {

                          /**
                           *
                           * По хэшу ищем транзакции и запрашиваем у них статус...
                           * Если все ок, то пишем статус ожидания события, иначе в отстойник
                           *
                           * TODO: Запрашивать после кол-ва блоков форка или по отсутствию события после n-го кол-ва блоков --- ???
                           *
                           */
                          WhileExists.process(() -> swapService.findSwaps(SwapStatus.SWAP_OPPOSITE_CHAIN_TX_SENT, swapDir), swaps ->
                              swaps.forEach(swap ->
                                  swapService.updateSwap(oppositeChainService.getTxReceipt(swap.getFinalizeTxHash()).fold(error ->
                                          // FIXME: needs to different errors like timeout exception and others
                                          swap.updateError(SwapStatus.SWAP_OPPOSITE_HASH_IS_NULL, error),
                                      txReceiptOpt ->
                                          txReceiptOpt.isEmpty() ?
                                              swap.updateError(SwapStatus.SWAP_OPPOSITE_HASH_IS_NULL, "Hash is null") :
                                              swap.updateFinalized(SwapStatus.SWAP_OPPOSITE_CHAIN_WAITING_FINALIZE_EVENT,
                                                  TxHashFactory.create(txReceiptOpt.get().getTransactionHash()))))));

                          // TODO: Check for exceptions - in case of exceptions sync process must die - in all cases
                          WhileExists.process(() -> swapService.findSwaps(SwapStatus.SWAP_OPPOSITE_HASH_IS_NULL, swapDir),
                              swaps ->
                                  swapService.updateSwaps(
                                      StreamEx.of(swaps).map(swap -> swap.update(SwapStatus.SWAP_OPPOSITE_CHAIN_TX_SENT)).toList()));


                          swapPairService.findActiveSwapPair("10SET").ifPresentOrElse(swapPair ->


                                  /**
                                   *
                                   * 1. Ставим статус SwapStatus.SWAP_TX_SENDING - это значит мы начинаем подготовку транзакции к отправке
                                   *
                                   * 2. Создаем транзакцию и отправляем, статус у свопа ставим SWAP_TX_SENT - проблема nonce проявится?
                                   *
                                   */
                                  WhileExists.process(() -> swapService.findSwaps(SwapStatus.SWAP_START_EVENT_RECEIVED, swapDir), swaps ->
                                      swaps.forEach(swap ->
                                          swapService.updateSwap(
                                              oppositeChainService.isFinalizedSwap(
                                                  swap.getStartTxHash()).fold(error -> swap.updateError(SwapStatus.SWAP_FINALIZED_RECHECK, error)
                                                  , finalized -> {
                                                    if (finalized)
                                                      return swap.update(SwapStatus.SWAP_OPPOSITE_CHAIN_WAITING_FINALIZE_EVENT);
                                                    else
                                                      return oppositeChainService.getFreeTokens().fold(
                                                          error -> swap.updateError(SwapStatus.SWAP_FINALIZATION_RETRY, error),
                                                          freeTokens -> {
                                                            if (freeTokens.getValue().compareTo(swap.getAmount().getValue()) == 1)
                                                              return oppositeChainService.finalizeSwap(
                                                                  swap.getStartTxHash(),
                                                                  swap.getAddress(),
                                                                  swap.getAmount()).fold(error ->
                                                                      // Временные ошибки, после которых нужна перепроверка
                                                                      (error.getCode() == Error.EC_BALANCE_LIMIT_EXCEEDED ||
                                                                          error.getCode() == Error.EC_CAN_NOT_GET_OPTIONS ||
                                                                          error.getCode() == Error.EC_REQUESTS_LIMIT_EXCEEDED ||
                                                                          error.getCode() == Error.EC_GAS_PRICES_LIMIT_EXCEEDED ||
                                                                          error.getCode() == Error.EC_CAN_NOT_GET_BALANCE ||
                                                                          error.getCode() == Error.EC_SOCKET_TIMEOUT_EXCEPTION ||
                                                                          error.getCode() == Error.EC_NONCE_TOOL_LOW) ?
                                                                          // TODO: perform socket timeout exception and others
                                                                          swap.updateError(SwapStatus.SWAP_FINALIZATION_RETRY, error.getDescr()) :
                                                                          swap.updateError(SwapStatus.SWAP_OPPOSITE_CHAIN_TX_SENDING_FAILED, error)
                                                                  , txHash -> swap.updateFinalized(SwapStatus.SWAP_OPPOSITE_CHAIN_TX_SENT, txHash)
                                                              );
                                                            else
                                                              return swap.updateError(SwapStatus.SWAP_FINALIZATION_RETRY, "Free tokens on " +
                                                                  oppositeChain + " swap contract is " + freeTokens.getValue().toString() +
                                                                  " less than tokens for swap " + swap.getAmount().getValue().toString() +
                                                                  " for swap start tx " + swap.getStartTxHash() +
                                                                  ". You should fill swapper contract tokens balance." +
                                                                  "Now I set transaction to retry state while balance will not be enough.");
                                                          });
                                                  })))),
                              () -> log.warn("No one active swap pairs!"));

                          // Возвращем SWAP_FINALIZED_RECHECK на SWAP_FINALIZATION_RETRY для того чтобы в последствие проверить
                          swapService.updateChainSwapsStatus(swapDir, SwapStatus.SWAP_FINALIZATION_RETRY, SwapStatus.SWAP_START_EVENT_RECEIVED);
                          // Возвращем SWAP_FINALIZED_RECHECK на SWAP_START_EVENT_RECEIVED для того чтобы в последствие проверить
                          swapService.updateChainSwapsStatus(swapDir, SwapStatus.SWAP_FINALIZED_RECHECK, SwapStatus.SWAP_START_EVENT_RECEIVED);

                        }
                      });
                    }
                  });
                }));
  }


}


