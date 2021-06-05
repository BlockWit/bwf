package com.blockwit.bwf.service.chains.common.events.sigs;

import java.util.List;

public class EventSignatureSwapFinalized extends ChainEventSignature {

	public static final String NAME = "SwapFinalized";

	public EventSignatureSwapFinalized() {
		super(NAME, List.of(
			new ChainEventFieldParam("tokenAddr", ChainTypes.address, true),
			new ChainEventFieldParam("otherTxHash", ChainTypes.bytes32, true),
			new ChainEventFieldParam("toAddr", ChainTypes.address, true),
			new ChainEventFieldParam("amount", ChainTypes.uint256, false)
		));
	}

}
