package com.blockwit.bwf.service.chains.gasproviders;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;

import java.math.BigInteger;

public class StaticOptionsBasedGasProvider extends WithDefaultOptionsBasedGasProvider {

  public StaticOptionsBasedGasProvider(Chains chain, OptionService optionService) {
    super(chain, optionService);
  }

  @Override
  public BigInteger getGasPrice() {
    return getGasPriceCachedOrDefault();
  }

  @Override
  public BigInteger getGasLimit() {
    return getGasLimitCachedOrDefault();
  }

}
