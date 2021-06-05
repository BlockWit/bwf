package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.SwapPair;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SwapPairViewMapper implements IMapper<SwapPair, SwapPairView> {

  private final OptionService optionService;

  public SwapPairViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<SwapPairView> map(List<SwapPair> swapPairs, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_TOKEN_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(swapPairs)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
  }

  @Override
  public Optional<SwapPairView> map(SwapPair swapPair, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_TOKEN_LINK)).flatMap(Collection::stream).toList(),
        options -> fromModelToView(swapPair, options));
  }

  private static Optional<SwapPairView> fromModelToView(SwapPair model,
                                                        Map<String, Option> options) {
    String ethTokenLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_TOKEN_LINK).getValue();
    String bscTokenLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_TOKEN_LINK).getValue();

    return Optional.of(SwapPairView.builder()
        .id(model.getId())
        .status(model.getStatus().name())
        .bep20Addr(model.getBep20Addr().getAddress())
        .erc20Addr(model.getEth20Addr().getAddress())
        .symbol(model.getSymbol())
        .name(model.getName())
        .decimals(model.getDecimals())
        .createTime(model.getCreateTime() == null ? "" : dateTimeFormatter.print(model.getCreateTime()))
        .bep20AddrLink(bscTokenLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getBep20Addr().getAddress()))
        .erc20AddrLink(ethTokenLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getEth20Addr().getAddress()))
        .build());
  }

}
