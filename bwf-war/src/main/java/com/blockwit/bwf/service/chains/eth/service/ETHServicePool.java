package com.blockwit.bwf.service.chains.eth.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.service.ChainServicePool;
import org.springframework.stereotype.Component;

@Component
public class ETHServicePool extends ChainServicePool {

  public ETHServicePool(ETHServicePoolFactory factory, OptionService optionService) {
    super(Chains.Ethereum,
        factory,
        optionService);
  }

}
