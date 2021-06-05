package com.blockwit.bwf.service.chains.bsc.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.bsc.service.events.BSCEventFactory;
import com.blockwit.bwf.service.chains.common.service.ChainServicePoolFactory;
import org.springframework.stereotype.Component;

@Component
public class BSCServicePoolFactory extends ChainServicePoolFactory {

	public BSCServicePoolFactory(OptionService optionService,
															 BSCEventFactory eventFactory) {
		super(Chains.BinanceSmartChain,
			optionService,
			eventFactory);
	}

}
