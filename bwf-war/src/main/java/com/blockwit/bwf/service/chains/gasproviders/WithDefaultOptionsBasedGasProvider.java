package com.blockwit.bwf.service.chains.gasproviders;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;

@Slf4j
public abstract class WithDefaultOptionsBasedGasProvider implements IGasProvider {

  public static final BigInteger DEFAULT_GAS_PRICE = new BigInteger("20000000000");

  public static final BigInteger DEFAULT_GAS_LIMIT = new BigInteger("200000");

  public OptionService optionService;

  public Chains chain;

  protected BigInteger gasPrice;

  protected BigInteger gasLimit;

  public WithDefaultOptionsBasedGasProvider(Chains chain, OptionService optionService) {
    this.optionService = optionService;
    this.chain = chain;
  }

  protected BigInteger getGasPriceCachedOrDefault() {
    gasPrice = gasPrice == null ?
        WithOptionsProfiledResult.process(optionService,
            List.of(chain + OptionService.OPTION_Chain_GAS_PRICE),
            options -> (BigInteger) options.get(chain + OptionService.OPTION_Chain_GAS_PRICE).getPerformedValue()
            , () -> {
              log.warn("Cant read default gas price property for " + chain + ". Will return default " + DEFAULT_GAS_PRICE);
              return DEFAULT_GAS_PRICE;
            })
        :
        gasPrice;
    return gasPrice;
  }

  protected BigInteger getGasLimitCachedOrDefault() {
    gasLimit = gasLimit == null ?
        WithOptionsProfiledResult.process(optionService,
            List.of(Chains.Ethereum + OptionService.OPTION_Chain_GAS_LIMIT),
            options -> (BigInteger) options.get(Chains.Ethereum + OptionService.OPTION_Chain_GAS_LIMIT).getPerformedValue()
            , () -> {
              log.warn("Cant read default gas limit property for " + chain + ". Will return default " + DEFAULT_GAS_LIMIT);
              return DEFAULT_GAS_LIMIT;
            })
        :
        gasLimit;
    return gasLimit;
  }

}
