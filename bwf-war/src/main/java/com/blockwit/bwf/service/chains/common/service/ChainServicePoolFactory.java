package com.blockwit.bwf.service.chains.common.service;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.IChainService;
import com.blockwit.bwf.service.chains.common.events.IEventFactory;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiled;
import com.blockwit.bwf.service.chains.gasproviders.DefiPulseGasProvider;
import com.blockwit.bwf.service.chains.gasproviders.IGasProvider;
import com.blockwit.bwf.service.chains.gasproviders.StaticOptionsBasedGasProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ChainServicePoolFactory extends BasePooledObjectFactory<IChainService> {

  private final OptionService optionService;

  private final IEventFactory eventFactory;

  private Chains chain;

  public ChainServicePoolFactory(Chains chain,
                                 OptionService optionService,
                                 IEventFactory eventFactory) {
    this.chain = chain;
    this.optionService = optionService;
    this.eventFactory = eventFactory;
  }

  @Override
  public synchronized IChainService create() throws Exception {
    Optional<Option> option = optionService.findProfiledByName(chain + OptionService.OPTION_Chain_PROVIDER);
    String chainProvider = option.get().getValue();
    HttpService httpService = new HttpService(chainProvider);
    Web3j web3j = Web3j.build(httpService);

    return WithOptionsProfiled.processF(optionService,
        List.of(chain + OptionService.OPTION_Chain_SWAPPER_ADDR,
            chain + OptionService.OPTION_Chain_INVOKER_PK,
            chain + OptionService.OPTION_Chain_GAS_PRICE_PROVIDER),
        options -> {
          String invokerPk = (String) options.get(chain + OptionService.OPTION_Chain_INVOKER_PK).getPerformedValue();
          IChainAddress swapAddr = (IChainAddress) options.get(chain + OptionService.OPTION_Chain_SWAPPER_ADDR).getPerformedValue();
          String gasProviderString = (String) options.get(chain + OptionService.OPTION_Chain_GAS_PRICE_PROVIDER).getPerformedValue();

          IGasProvider gasProvider;
          if (gasProviderString.equals(StaticOptionsBasedGasProvider.class.getSimpleName()))
            gasProvider = new StaticOptionsBasedGasProvider(chain, optionService);
          else if (gasProviderString.equals(DefiPulseGasProvider.class.getSimpleName())) {
            if (!chain.equals(Chains.Ethereum)) {
              log.warn("Unknown DEFI Pulse gas provider not compatible with not Ethererum chains. Default static gas will created");
              gasProvider = new StaticOptionsBasedGasProvider(chain, optionService);
            } else
              gasProvider = new DefiPulseGasProvider(optionService);
          } else {
            log.warn("Unknown Gas Provider name \"" + gasProviderString + "\". Default static gas will created");
            gasProvider = new StaticOptionsBasedGasProvider(chain, optionService);
          }

          return new ChainService(chain,
              web3j,
              optionService,
              eventFactory,
              swapAddr,
              invokerPk,
              gasProvider);
        });
  }

  @Override
  public synchronized PooledObject<IChainService> wrap(IChainService connector) {
    return new DefaultPooledObject<>(connector);
  }

  @Override
  public synchronized void destroyObject(PooledObject<IChainService> p) {
    p.getObject().destroy();
  }

}
