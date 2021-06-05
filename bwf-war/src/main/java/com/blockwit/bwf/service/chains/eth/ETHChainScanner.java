package com.blockwit.bwf.service.chains.eth;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.SchedTasksService;
import com.blockwit.bwf.service.SwapPairService;
import com.blockwit.bwf.service.SwapService;
import com.blockwit.bwf.service.chains.bsc.service.BSCServicePool;
import com.blockwit.bwf.service.chains.common.AbstractChainScanner;
import com.blockwit.bwf.service.chains.common.utils.ChainHelper;
import com.blockwit.bwf.service.chains.eth.service.ETHServicePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ETHChainScanner extends AbstractChainScanner {

  public ETHChainScanner(ChainHelper chainHelper,
                         ETHServicePool chainServicePool,
                         BSCServicePool oppositeChainServicePool,
                         OptionService optionService,
                         SwapService swapService,
                         SwapPairService swapPairService,
                         SchedTasksService schedTasksService) {
    super(Chains.Ethereum,
        chainHelper,
        chainServicePool,
        oppositeChainServicePool,
        optionService,
        swapService,
        swapPairService,
        schedTasksService);
  }

}
