package com.blockwit.bwf.service;

import com.blockwit.bwf.model.chain.SwapPair;
import com.blockwit.bwf.model.chain.SwapPairStatus;
import com.blockwit.bwf.model.chain.factories.ChainAddressFactory;
import com.blockwit.bwf.repository.SwapPairRepository;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class SwapPairService {

	private final SwapPairRepository swapPairRepository;

	public SwapPairService(SwapPairRepository swapPairRepository) {
		this.swapPairRepository = swapPairRepository;
		updateInitial();
	}

	@Transactional
	public Optional<SwapPair> findActiveSwapPair(String symbol) {
		return swapPairRepository.findBySymbolAndStatus(symbol, SwapPairStatus.SWAP_PAIR_ACTIVE);
	}

	@Transactional
	protected void updateInitial() {
		Set<SwapPair> defaultPairs = Set.of(
			new SwapPair(0L,
				SwapPairStatus.SWAP_PAIR_ACTIVE,
				ChainAddressFactory.create("0x37B6E0Fe1226CC1b6adec71a8Aa5920Aa27E63C4"),
				ChainAddressFactory.create("0x8350db2FF1e0974a082f13DAE06EdD2f4CeD3018"),
				"10SET",
				"10Set Token",
				18,
				System.currentTimeMillis()));

		Set<String> symbolsFromDB =
			StreamEx.of(swapPairRepository.findBySymbolIn(StreamEx.of(defaultPairs).map(SwapPair::getSymbol).toSet()))
				.map(SwapPair::getSymbol).toSet();

		StreamEx.of(defaultPairs).filter(t -> !symbolsFromDB.contains(t.getSymbol())).forEach(swapPairRepository::save);
	}

}
