package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.*;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.model.chain.fields.ITxLog;
import com.blockwit.bwf.repository.SwapFinalizeTxLogsRepository;
import com.blockwit.bwf.repository.SwapRepository;
import com.blockwit.bwf.repository.SwapStartTxLogsRepository;
import com.blockwit.bwf.repository.TxsRepository;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SwapService {

  private final SwapStartTxLogsRepository swapStartTxLogsRepository;

  private final SwapFinalizeTxLogsRepository swapFinalizeTxLogsRepository;

  private final SwapRepository swapRepository;

  private final TxsRepository txsRepository;

  private final OptionService optionService;

  public List<Swap> findLastSwaps(int count) {
    Pageable pageable = PageRequest.of(0, count, Sort.Direction.DESC, "id");
    return swapRepository.findAll(pageable).toList();
  }

  @Transactional
  public Either<Error, Swap> tryChangeStatus(long swapId,
                                             SwapStatus oldSwapStatus,
                                             SwapStatus newSwapStatus) {
    Optional<Swap> swapOptional = swapRepository.findById(swapId);
    if (swapOptional.isPresent()) {
      Swap swap = swapOptional.get();
      if (swap.getStatus().equals(oldSwapStatus)) {
        Swap updatedSwap = swapRepository.save(swap.update(newSwapStatus));
        if (updatedSwap == null)
          return Either.left(new Error(Error.EC_CAN_NOT_UPDATE_SWAP, Error.EM_CAN_NOT_UPDATE_SWAP + ": " +
              "Unknown error during swap " + swapId +
              " update status from " + oldSwapStatus + " to " + newSwapStatus));
        return Either.right(updatedSwap);
      } else {
        return Either.left(new Error(Error.EC_WRONG_SWAP_STATUS, Error.EM_WRONG_SWAP_STATUS + ": " +
            "Wrong swap status " + swap.getStatus() +
            ". It should be + " + oldSwapStatus));
      }
    }

    return Either.left(new Error(Error.EC_SWAP_NOT_FOUND, Error.EM_SWAP_NOT_FOUND +
        ": Swap with id " + swapId + " not found"));
  }

  @Getter
  @AllArgsConstructor
  public class SwapWithTxs {

    private Swap swap;

    private SwapStartTxLog swapStartTxLog;

  }

  public SwapService(SwapRepository swapRepository,
                     SwapStartTxLogsRepository swapStartTxLogsRepository,
                     SwapFinalizeTxLogsRepository swapFinalizeTxLogsRepository,
                     TxsRepository txsRepository,
                     OptionService optionService) {
    this.swapRepository = swapRepository;
    this.swapStartTxLogsRepository = swapStartTxLogsRepository;
    this.swapFinalizeTxLogsRepository = swapFinalizeTxLogsRepository;
    this.txsRepository = txsRepository;
    this.optionService = optionService;
  }


  @Transactional
  public List<Swap> updateChainSwapsStatus(SwapDirection swapDirection, SwapStatus statusFrom, SwapStatus statusTo) {
    return swapRepository.saveAll(StreamEx.of(swapRepository.findByDirectionAndStatus(swapDirection, statusFrom))
        .map(swap -> swap.update(statusTo)).toList());
  }

  @Transactional
  public void saveSwapStartLogsIfNotExistsAndUpdateOptions(List<ITxLog> swapLogs, Option currentBlock, Option startBlock) {
    _saveSwapStartLogsIfNotExists(swapLogs);
    optionService._update(currentBlock);
    optionService._update(startBlock);
  }

  @Transactional
  public void updateStartTxLogsChangeStatus(Chains chain, TxLogStatus oldStatus, TxLogStatus newStatus) {
    swapStartTxLogsRepository.saveAll(
        StreamEx.of(swapStartTxLogsRepository.findTop20ByChainAndStatusOrderByCreateTimeDesc(chain, oldStatus))
            .map(t -> t.updateStatus(newStatus)).toList());
  }


  public List<Swap> findSwaps(SwapStatus swapStatus, SwapDirection swapDirection) {
    return swapRepository.findTop20ByStatusAndDirectionOrderByCreateTimeDesc(swapStatus, swapDirection);
  }

  public List<Swap> findSwapsByStartTxHashAndStatus(List<ITxHash> startTxHashes, SwapStatus swapStatus) {
    return swapRepository.findByStartTxHashInAndStatus(startTxHashes, swapStatus);
  }

  public Optional<Swap> findSwapByStartTxHash(ITxHash finalizeTxHash) {
    return swapRepository.findByStartTxHash(finalizeTxHash);
  }

  public List<ITxLog> findTxLogs(Chains chain, TxLogStatus txLogStatus) {
    List<ITxLog> txLogs = new ArrayList<>();
    txLogs.addAll(findSwapStartTxLogs(chain, txLogStatus));
    txLogs.addAll(findSwapFinalizedTxLogs(chain, txLogStatus));
    return txLogs;
  }

  public List<SwapStartTxLog> findSwapStartTxLogs(Chains chain, TxLogStatus txLogStatus) {
    return swapStartTxLogsRepository.findTop20ByChainAndStatusOrderByCreateTimeDesc(chain, txLogStatus);
  }

  public List<SwapFinalizeTxLog> findSwapFinalizedTxLogs(Chains chain, TxLogStatus txLogStatus) {
    return swapFinalizeTxLogsRepository.findTop20ByChainAndStatusOrderByCreateTimeDesc(chain, txLogStatus);
  }

  public List<ITxLog> _saveSwapStartLogsIfNotExists(List<ITxLog> logs) {
    if (logs.isEmpty())
      return logs;

    List<ITxLog> saved = new ArrayList<>(logs.size());

    saved.addAll(_saveSwapStartTxLogsIfNotExists(logs.stream()
        .filter(t -> t instanceof SwapStartTxLog)
        .map(t -> ((SwapStartTxLog) t))
        .collect(Collectors.toList())));

    saved.addAll(_saveSwapFinalizeTxLogsIfNotExists(logs.stream()
        .filter(t -> t instanceof SwapFinalizeTxLog)
        .map(t -> ((SwapFinalizeTxLog) t))
        .collect(Collectors.toList())));

    return saved;
  }


  private List<SwapFinalizeTxLog> _saveSwapFinalizeTxLogsIfNotExists(List<SwapFinalizeTxLog> logs) {
    if (logs.isEmpty())
      return logs;

    Set<ITxHash> exists = StreamEx.of(swapFinalizeTxLogsRepository.findByTxHashIn(
        StreamEx.of(logs)
            .map(SwapFinalizeTxLog::getTxHash).toList()))
        .map(SwapFinalizeTxLog::getTxHash).toSet();

    List<SwapFinalizeTxLog> toSave = swapFinalizeTxLogsRepository.saveAll(
        StreamEx.of(logs).filter(log -> !exists.contains(log.getTxHash())));

    return swapFinalizeTxLogsRepository.saveAll(toSave);
  }

  public List<SwapFinalizeTxLog> findSwapFinalizedTxLogsByStartHashes(List<ITxHash> hashes, TxLogStatus status) {
    return swapFinalizeTxLogsRepository.findByCreateTxHashInAndStatus(hashes, status);
  }

  private List<SwapStartTxLog> _saveSwapStartTxLogsIfNotExists(List<SwapStartTxLog> logs) {
    if (logs.isEmpty())
      return logs;

    Set<ITxHash> exists = StreamEx.of(swapStartTxLogsRepository.findByTxHashIn(
        StreamEx.of(logs)
            .map(SwapStartTxLog::getTxHash).toList()))
        .map(SwapStartTxLog::getTxHash).toSet();

    List<SwapStartTxLog> toSave = swapStartTxLogsRepository.saveAll(
        StreamEx.of(logs).filter(log -> !exists.contains(log.getTxHash())));

    return swapStartTxLogsRepository.saveAll(toSave);
  }

  @Transactional
  public List<Swap> updateSwaps(List<Swap> swaps) {
    return swapRepository.saveAll(swaps);
  }

  @Transactional
  public Swap updateSwap(Swap swap) {
    return swapRepository.save(swap);
  }

  @Transactional
  public Swap updateSwapAndFinalizeLog(Swap swap, SwapFinalizeTxLog swapFinalizeTxLog) {
    swapFinalizeTxLogsRepository.save(swapFinalizeTxLog);
    return swapRepository.save(swap);
  }

  @Transactional
  public List<Swap> updateSwapsAndStartLogs(List<Swap> swaps, List<SwapStartTxLog> swapStartTxLogs) {
    swapStartTxLogsRepository.saveAll(swapStartTxLogs);
    return swapRepository.saveAll(swaps);
  }

  @Transactional
  public List<SwapStartTxLog> updateStartTxLogs(List<SwapStartTxLog> swapStartTxLogs) {
    return swapStartTxLogsRepository.saveAll(swapStartTxLogs);
  }

  @Transactional
  public List<ITxLog> updateTxLogs(List<ITxLog> txLogs) {
    return _updateTxLogs(txLogs);
  }

  @Transactional
  public ITxLog updateTxLog(ITxLog txLog) {
    return _updateTxLog(txLog);
  }

  @Transactional
  public ITxLog updateTxLogAndCreateTx(ITxLog txLog, Tx tx) {
    updateTxLog(txLog);
    txsRepository.save(tx);
    return txLog;
  }

  private List<ITxLog> _updateTxLogs(List<ITxLog> txLogs) {
    List<SwapStartTxLog> swapStartTxLogs = new ArrayList<>();
    List<SwapFinalizeTxLog> swapFinalizeTxLogs = new ArrayList<>();
    txLogs.forEach(txLog -> {
      if (txLog instanceof SwapStartTxLog)
        swapStartTxLogs.add((SwapStartTxLog) txLog);
      else if (txLog instanceof SwapFinalizeTxLog)
        swapFinalizeTxLogs.add((SwapFinalizeTxLog) txLog);
      else
        log.error("Unknown tx log : " + txLog.getClass().getSimpleName());
    });

    List<ITxLog> resTxLogs = new ArrayList<>();
    if (!swapStartTxLogs.isEmpty())
      resTxLogs.addAll(swapStartTxLogsRepository.saveAll(swapStartTxLogs));

    if (!swapFinalizeTxLogs.isEmpty())
      resTxLogs.addAll(swapFinalizeTxLogsRepository.saveAll(swapFinalizeTxLogs));

    return resTxLogs;
  }

  private ITxLog _updateTxLog(ITxLog txLog) {
    if (txLog instanceof SwapStartTxLog)
      return swapStartTxLogsRepository.save((SwapStartTxLog) txLog);
    else if (txLog instanceof SwapFinalizeTxLog)
      return swapFinalizeTxLogsRepository.save((SwapFinalizeTxLog) txLog);
    else
      log.error("Unknown log type " + txLog.getClass().getSimpleName());
    return null;
  }


  @Transactional
  public void createSwapsAndUpdateStartTxLogs(List<SwapStartTxLog> swapStartTxLogs, List<Swap> swaps) {
    swapRepository.saveAll(swaps);
    swapStartTxLogsRepository.saveAll(swapStartTxLogs); // TODO: Check It
  }

}
