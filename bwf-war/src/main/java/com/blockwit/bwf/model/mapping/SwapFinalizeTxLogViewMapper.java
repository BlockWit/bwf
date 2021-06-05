package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.SwapFinalizeTxLog;
import com.blockwit.bwf.model.chain.SwapStartTxLog;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SwapFinalizeTxLogViewMapper implements IMapper<SwapFinalizeTxLog, SwapFinalizeTxLogView> {

  private final OptionService optionService;

  public SwapFinalizeTxLogViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<SwapFinalizeTxLogView> map(List<SwapFinalizeTxLog> swapFinalizeTxLogs, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_TOKEN_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(swapFinalizeTxLogs)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
  }

  @Override
  public Optional<SwapFinalizeTxLogView> map(SwapFinalizeTxLog swapFinalizeTxLog, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_TOKEN_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> fromModelToView(swapFinalizeTxLog, options));
  }

  private static Optional<SwapFinalizeTxLogView> fromModelToView(SwapFinalizeTxLog model,
                                                              Map<String, Option> options) {


    String ethAddressLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String ethTokenLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_TOKEN_LINK).getValue();
    String ethTxHashLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();
    String ethBlockNumberLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK).getValue();

    String bscAddressLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String bscTokenLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_TOKEN_LINK).getValue();
    String bscTxHashLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();
    String bscBlockNumberLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK).getValue();

    return Optional.of(SwapFinalizeTxLogView.builder()
        .id(model.getId())
        .chain(model.getChain().equals(Chains.Ethereum) ? "ETH" : "BSC")
        .tokenAddr(model.getTokenAddr().getAddress())
        .toAddr(model.getToAddr().getAddress())
        .amount(model.getAmount().getFormattedString())
        .txHash(model.getTxHash().getHash())
        .createTxHash(model.getCreateTxHash().getHash())
        .blockHash(model.getBlockHash().getHash())
        .blockNumber(model.getHeight().getNumber().toString())
        .status(model.getStatus().name())
        .createTime(model.getCreateTime() == null ? "" : dateTimeFormatter.print(model.getCreateTime()))
        .updateTime(model.getUpdateTime() == null ? "" : dateTimeFormatter.print(model.getUpdateTime()))
        .tokenAddrLink(model.getChain().equals(Chains.Ethereum) ?
            ethTokenLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getTokenAddr().getAddress()) :
            bscTokenLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getTokenAddr().getAddress()))
        .toAddrLink(model.getChain().equals(Chains.Ethereum) ?
            ethAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getToAddr().getAddress()) :
            bscAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getToAddr().getAddress()))
        .txHashLink(model.getChain().equals(Chains.Ethereum) ?
            ethTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getTxHash().getHash()) :
            bscTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getTxHash().getHash()))
        .createTxHashLink(model.getChain().equals(Chains.Ethereum) ?
            bscTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getCreateTxHash().getHash()) :
            ethTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getCreateTxHash().getHash()))
        .blockHashLink(model.getChain().equals(Chains.Ethereum) ?
            ethBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()) :
            bscBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()))
        .blockNumberLink(model.getChain().equals(Chains.Ethereum) ?
            ethBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()) :
            bscBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()))
        .build());
  }

}
