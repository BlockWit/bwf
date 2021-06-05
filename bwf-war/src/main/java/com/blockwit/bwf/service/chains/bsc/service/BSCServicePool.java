package com.blockwit.bwf.service.chains.bsc.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.service.ChainServicePool;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class BSCServicePool extends ChainServicePool {

  public BSCServicePool(BSCServicePoolFactory factory, OptionService optionService) {
    super(Chains.BinanceSmartChain,
        factory,
        optionService);
  }

}

