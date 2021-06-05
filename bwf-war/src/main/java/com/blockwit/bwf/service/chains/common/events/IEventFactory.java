package com.blockwit.bwf.service.chains.common.events;

import org.web3j.protocol.core.methods.response.EthLog;

import java.util.Optional;

public interface IEventFactory {

	Optional<ChainSimpleEvent> apply(EthLog.LogResult ethLogResult);

}
