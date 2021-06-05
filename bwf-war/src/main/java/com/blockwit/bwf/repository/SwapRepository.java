package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.chain.Swap;
import com.blockwit.bwf.model.chain.SwapDirection;
import com.blockwit.bwf.model.chain.SwapStatus;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwapRepository extends JpaRepository<Swap, Long> {

  Page<Swap> findAll(Pageable pageable);

  List<Swap> findTop20ByStatusAndDirectionOrderByCreateTimeDesc(SwapStatus swapStatus,
                                                                SwapDirection swapDirection);

  Optional<Swap> findByStartTxHash(ITxHash txHash);

  List<Swap> findByStartTxHashIn(List<ITxHash> txHashes);

  List<Swap> findByStartTxHashInAndStatus(List<ITxHash> txHashes, SwapStatus swapStatus);

  List<Swap> findByDirectionAndStatus(SwapDirection swapDirection, SwapStatus swapStatus);

}


