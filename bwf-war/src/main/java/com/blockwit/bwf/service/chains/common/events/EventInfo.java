package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.model.chain.Chains;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.web3j.protocol.core.methods.response.Log;

@AllArgsConstructor
@Getter
public class EventInfo {

	private Chains chain;

	private Log log;

	private ChainSimpleEvent simpleEvent;

}
