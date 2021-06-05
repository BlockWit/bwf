package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.chain.SwapPair;
import com.blockwit.bwf.model.chain.SwapPairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SwapPairRepository extends JpaRepository<SwapPair, Long> {

	Page<SwapPair> findAll(Pageable pageable);

	Optional<SwapPair> findBySymbol(String symbol);

	List<SwapPair> findBySymbolIn(Collection<String> symbols);

	Optional<SwapPair> findBySymbolAndStatus(String symbol, SwapPairStatus status);

}


