package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.SwapFinalizeTxLog;
import com.blockwit.bwf.model.chain.TxLogStatus;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapFinalizeTxLogsRepository extends JpaRepository<SwapFinalizeTxLog, Long> {

  List<SwapFinalizeTxLog> findByTxHashIn(List<ITxHash> txHashes);

  Page<SwapFinalizeTxLog> findAll(Pageable pageable);

  List<SwapFinalizeTxLog> findTop20ByChainAndStatusOrderByCreateTimeDesc(Chains chain, TxLogStatus txLogStatus);

  List<SwapFinalizeTxLog> findByCreateTxHashInAndStatus(List<ITxHash> hashes, TxLogStatus status);
}
