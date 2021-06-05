package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.SwapStartTxLog;
import com.blockwit.bwf.model.chain.TxLogStatus;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapStartTxLogsRepository extends JpaRepository<SwapStartTxLog, Long> {

	Page<SwapStartTxLog> findAll(Pageable pageable);

	List<SwapStartTxLog> findTop20ByChainAndStatusOrderByCreateTimeDesc(Chains chain,
																																			TxLogStatus txLogStatus);

	List<SwapStartTxLog> findByTxHashIn(List<ITxHash> txHashes);


}
