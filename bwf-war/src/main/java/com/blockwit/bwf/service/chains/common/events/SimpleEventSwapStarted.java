package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.factories.ChainAddressFactory;
import com.blockwit.bwf.model.chain.factories.ChainNumberFactory;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapStarted;
import lombok.Getter;
import org.web3j.protocol.core.methods.response.Log;

@Getter
public class SimpleEventSwapStarted extends ChainSimpleEvent {

	private IChainAddress tokenAddr;

	private IChainAddress fromAddr;

	private IChainNumber amount;

	private IChainNumber feeAmount;

	public SimpleEventSwapStarted(Chains chain, EventSignatureSwapStarted sign, Log log) {
		super(chain, sign, log);
		this.tokenAddr = ChainAddressFactory.create(fields.get("tokenAddr"));
		this.fromAddr = ChainAddressFactory.create(fields.get("fromAddr"));
		this.amount = ChainNumberFactory.create(fields.get("amount"));
		this.feeAmount = ChainNumberFactory.create(fields.get("feeAmount"));
	}

}
