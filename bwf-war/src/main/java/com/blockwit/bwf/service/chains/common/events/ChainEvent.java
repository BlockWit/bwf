package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.service.chains.common.events.sigs.ChainEventSignature;
import org.web3j.protocol.core.methods.response.Log;

import java.util.Map;

public class ChainEvent {

	private ChainEventSignature sign;

	private Log log;

	protected Map<String, String> fields;

	protected long timestamp;

	public ChainEvent(ChainEventSignature sign, Log log, long timestamp) {
		this.sign = sign;
		this.log = log;
		this.timestamp = timestamp;
		this.fields = EventHelper.logToFields(sign, log);
	}

	@Override
	public String toString() {
		return EventHelper.toString(sign, fields);
	}

}
