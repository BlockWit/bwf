package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.chains.common.events.sigs.ChainEventSignature;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Map;

public class ChainSimpleEvent {

	private Chains chain;

	private ChainEventSignature sign;

	private Log log;

	protected Map<String, String> fields;

	public ChainSimpleEvent(Chains chain, ChainEventSignature sign, Log log) {
		this.chain = chain;
		this.sign = sign;
		this.log = log;
		this.fields = EventHelper.logToFields(sign, log);
	}

	@Override
	public String toString() {
		return EventHelper.toString(sign, fields);
	}

	public String getEventName() {
		return sign.getName();
	}

}
