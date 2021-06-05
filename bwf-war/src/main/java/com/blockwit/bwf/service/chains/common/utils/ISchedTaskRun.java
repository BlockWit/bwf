package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.service.chains.common.IChainService;

@FunctionalInterface
public interface ISchedTaskRun {

  void process(IChainService chainService, IChainService oppositeChainService);

}
