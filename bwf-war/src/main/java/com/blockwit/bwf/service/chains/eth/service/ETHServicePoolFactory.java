package com.blockwit.bwf.service.chains.eth.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.service.ChainServicePoolFactory;
import com.blockwit.bwf.service.chains.eth.service.events.ETHEventFactory;
import org.springframework.stereotype.Component;

@Component
public class ETHServicePoolFactory extends ChainServicePoolFactory {

	public ETHServicePoolFactory(OptionService optionService, ETHEventFactory eventFactory) {
		super(Chains.Ethereum,
			optionService,
			eventFactory);
	}

}
