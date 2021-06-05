package com.blockwit.bwf.service.chains.common.events.sigs;

import java.util.List;

public class EventSignatureSwapStarted extends ChainEventSignature {

	public static final String NAME = "SwapStarted";

	public EventSignatureSwapStarted() {
		super(NAME, List.of(
			new ChainEventFieldParam("tokenAddr", ChainTypes.address, true),
			new ChainEventFieldParam("fromAddr", ChainTypes.address, true),
			new ChainEventFieldParam("amount", ChainTypes.uint256, false),
			new ChainEventFieldParam("feeAmount", ChainTypes.uint256, false)
		));
	}

}
