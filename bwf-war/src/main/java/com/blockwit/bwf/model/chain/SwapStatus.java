package com.blockwit.bwf.model.chain;

public enum SwapStatus {

  SWAP_START_EVENT_RECEIVED, // пользователь начал своп
  SWAP_REJECTED, // Если что-то пощло не так при проверке
  SWAP_OPPOSITE_CHAIN_TX_SENDING,
  SWAP_OPPOSITE_CHAIN_TX_SENDING_FAILED,
  SWAP_OPPOSITE_CHAIN_TX_RECEIPT_FAILED,
  SWAP_OPPOSITE_CHAIN_TX_SENT,
  SWAP_OPPOSITE_HASH_IS_NULL,
  SWAP_OPPOSITE_CHAIN_WAITING_FINALIZE_EVENT,
  SWAP_OPPOSITE_CHAIN_TX_FAILED,
  SWAP_FINALIZED_RECHECK, // устанавливается, когда еще не обработали finalize событие, но транзакция уже обработана
  // Сбрасывается в SWAP_OPPOSITE_CHAIN_TX_SENT после этого, на случай форков (потом это состояние может пропасть)
  // Или если получение инфы не прошло успешно

  SWAP_FINISHED,
  SWAP_OLD,
  SWAP_FINALIZATION_RETRY

}
