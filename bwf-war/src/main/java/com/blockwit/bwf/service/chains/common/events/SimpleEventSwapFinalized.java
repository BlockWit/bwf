package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.factories.ChainAddressFactory;
import com.blockwit.bwf.model.chain.factories.ChainNumberFactory;
import com.blockwit.bwf.model.chain.factories.TxHashFactory;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapFinalized;
import lombok.Getter;
import org.web3j.protocol.core.methods.response.Log;

@Getter
public class SimpleEventSwapFinalized extends ChainSimpleEvent {

	private IChainAddress tokenAddr;

	private IChainAddress toAddr;

	private ITxHash createTxHash;

	private IChainNumber amount;

	public SimpleEventSwapFinalized(Chains chain, EventSignatureSwapFinalized sign, Log log) {
		super(chain, new EventSignatureSwapFinalized(), log);
		this.tokenAddr = ChainAddressFactory.create(fields.get("tokenAddr"));
		this.toAddr = ChainAddressFactory.create(fields.get("toAddr"));
		this.createTxHash = TxHashFactory.create(fields.get("otherTxHash"));
		this.amount = ChainNumberFactory.create(fields.get("amount"));
	}

}
