package com.blockwit.bwf.service.chains.gasproviders;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefiPulseGasProvider extends StaticOptionsBasedGasProvider {

  public static final BigInteger DEFIPULSE_GAS_PRICE_MUL = new BigInteger("100000000");

  public static final String FAST_FIELD = "fast";

  ObjectMapper mapper = new ObjectMapper();

  public DefiPulseGasProvider(OptionService optionService) {
    super(Chains.Ethereum, optionService);
  }

  @Override
  public BigInteger getGasPrice() {
    gasPrice = WithOptions.processF(optionService,
        List.of(OptionService.OPTION_DEFIPULSE_URL),
        options -> {
          String defiPulseURL = (String) options.get(OptionService.OPTION_DEFIPULSE_URL).getPerformedValue();

          Map<String, Object> map;

          try {
            map = mapper.readValue(new URL(defiPulseURL), Map.class);
          } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while getting gas price", e);
            return getGasPriceCachedOrDefault();
          }

          try {
            return new BigInteger((String) map.get(FAST_FIELD)).multiply(DEFIPULSE_GAS_PRICE_MUL);
          } catch (Exception e) {
            log.error("Error while getting gas price", e);
            return getGasPriceCachedOrDefault();
          }

        }, () -> getGasPriceCachedOrDefault()
    );
    return gasPrice;
  }

}
