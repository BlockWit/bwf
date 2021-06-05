package com.blockwit.bwf.service.chains.common.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.IChainService;
import com.blockwit.bwf.service.chains.common.IChainServicePool;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiled;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.DisposableBean;

import java.util.List;

public class ChainServicePool implements IChainServicePool<IChainService>, DisposableBean {

  private GenericObjectPool<IChainService> pool;

  public ChainServicePool(Chains chain,
                          ChainServicePoolFactory factory,
                          OptionService optionService) {
    WithOptionsProfiled.process(optionService,
        List.of(chain + OptionService.OPTION_Chain_SERVICE_POOL_MAX_TOTAL,
            chain + OptionService.OPTION_Chain_SERVICE_POOL_MAX_IDLE,
            chain + OptionService.OPTION_Chain_SERVICE_POOL_MIN_IDLE),
        options -> {
          pool = new GenericObjectPool(factory);
          pool.setMaxTotal((Integer) options.get(chain + OptionService.OPTION_Chain_SERVICE_POOL_MAX_TOTAL).getPerformedValue());
          pool.setMaxIdle((Integer) options.get(chain + OptionService.OPTION_Chain_SERVICE_POOL_MAX_IDLE).getPerformedValue());
          pool.setMinIdle((Integer) options.get(chain + OptionService.OPTION_Chain_SERVICE_POOL_MIN_IDLE).getPerformedValue());
        });
  }

  public ChainServicePoolMetrics getMetrics() {
    return new ChainServicePoolMetrics(pool.getMaxTotal(), pool.getNumActive(), pool.getNumIdle());
  }

  @Override
  public IChainService borrowObject() throws Exception {
    return pool.borrowObject();
  }

  @Override
  public void returnObject(IChainService chainService) {
    pool.returnObject(chainService);
  }

  @Override
  public boolean isClosed() {
    return pool.isClosed();
  }

  @Override
  public void destroy() throws Exception {
    pool.close();
  }
}
