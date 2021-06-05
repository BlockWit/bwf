package com.blockwit.bwf.service.chains.common.events;

import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class EventFactory implements IEventFactory {

	protected Map<String, Function<Log, ChainSimpleEvent>> eventSignatureMap = new HashMap<>();

	@Override
	public Optional<ChainSimpleEvent> apply(EthLog.LogResult ethLogResult) {
		Object mayBeLog = ethLogResult.get();
		if (mayBeLog instanceof Log) {
			Log log = (Log) mayBeLog;
			String firstTopicSignature = log.getTopics().get(0);
			Function<Log, ChainSimpleEvent> function = eventSignatureMap.get(firstTopicSignature);
			if (function == null)
				return Optional.empty();

			return Optional.of(function.apply(log));
		}
		return Optional.empty();
	}

}
