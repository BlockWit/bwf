package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.Swap;
import com.blockwit.bwf.model.chain.SwapDirection;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SwapViewMapper implements IMapper<Swap, SwapView> {

  private final OptionService optionService;

  public SwapViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<SwapView> map(List<Swap> swaps, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(swaps)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
  }

  @Override
  public Optional<SwapView> map(Swap swap, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> fromModelToView(swap, options));
  }

  private static Optional<SwapView> fromModelToView(Swap model,
                                                    Map<String, Option> options) {

    String ethAddressLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String ethTxHashLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();

    String bscAddressLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String bscTxHashLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();

    return Optional.of(SwapView.builder()
        .id(model.getId())
        .status(model.getStatus().name())
        .address(model.getAddress().getAddress())
        .amount(model.getAmount().getFormattedString())
        .direction(model.getDirection().name())
        .startTxHash(model.getStartTxHash().getHash())
        .finalizeTxHash(model.getFinalizeTxHash() == null ? null : model.getFinalizeTxHash().getHash())
        .log(model.getLog())
        .prevLog(model.getPrevLog())
        .createTime(model.getCreateTime() == null ? "" : dateTimeFormatter.print(model.getCreateTime()))
        .updateTime(model.getUpdateTime() == null ? "" : dateTimeFormatter.print(model.getUpdateTime()))
        .addressLink(model.getDirection().equals(SwapDirection.ETH_TO_BSC) ?
            bscAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getAddress().getAddress()) :
            ethAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getAddress().getAddress()))
        .startTxHashLink(model.getDirection().equals(SwapDirection.ETH_TO_BSC) ?
            ethTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getStartTxHash().getHash()) :
            bscTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getStartTxHash().getHash()))
        .finalizeTxHashLink(model.getFinalizeTxHash() == null ? null : (model.getDirection().equals(SwapDirection.ETH_TO_BSC) ?
            bscTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getFinalizeTxHash().getHash()) :
            ethTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getFinalizeTxHash().getHash())))
        .build());
  }

}
