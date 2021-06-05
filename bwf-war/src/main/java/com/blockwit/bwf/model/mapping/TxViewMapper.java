package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.Tx;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TxViewMapper implements IMapper<Tx, TxView> {

  private final OptionService optionService;

  public TxViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<TxView> map(List<Tx> txs, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(txs)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
  }

  @Override
  public Optional<TxView> map(Tx tx, Object context) {
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> fromModelToView(tx, options));
  }

  private static Optional<TxView> fromModelToView(Tx model, Map<String, Option> options) {

    String ethAddressLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String ethTxHashLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();
    String ethBlockNumberLinkPattern = options.get(Chains.Ethereum + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK).getValue();

    String bscAddressLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_ADDRESS_LINK).getValue();
    String bscTxHashLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_TX_HASH_LINK).getValue();
    String bscBlockNumberLinkPattern = options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK).getValue();

    return Optional.of(TxView.builder()
        .id(model.getId())
        .chain(model.getChain().equals(Chains.Ethereum) ? "ETH" : "BSC")
        .status(model.getStatus().name())
        .nonce(model.getNonce().toString())
        .fromAddr(model.getFromAddr().getAddress())
        .toAddr(model.getToAddr().getAddress())
        .amount(model.getAmount().getFormattedString())
        .txHash(model.getTxHash().getHash())
        .blockHash(model.getBlockHash().getHash())
        .blockNumber(model.getHeight().getNumber().toString())
        .gas(model.getGas().getFormattedString())
        .gasPrice(model.getGasPrice().getFormattedString())
        .createTime(model.getCreateTime() == null ? "" : dateTimeFormatter.print(model.getCreateTime()))
        .updateTime(model.getUpdateTime() == null ? "" : dateTimeFormatter.print(model.getUpdateTime()))
        .fromAddrLink(model.getChain().equals(Chains.Ethereum) ?
            ethAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getFromAddr().getAddress()) :
            bscAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getFromAddr().getAddress()))
        .toAddrLink(model.getChain().equals(Chains.Ethereum) ?
            ethAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getFromAddr().getAddress()) :
            bscAddressLinkPattern.replace(OptionService.PATTERN_ADDRESS, model.getToAddr().getAddress()))
        .txHashLink(model.getChain().equals(Chains.Ethereum) ?
            ethTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getTxHash().getHash()) :
            bscTxHashLinkPattern.replace(OptionService.PATTERN_TX_HASH, model.getTxHash().getHash()))
        .blockHashLink(model.getChain().equals(Chains.Ethereum) ?
            ethBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()) :
            bscBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()))
        .blockNumberLink(model.getChain().equals(Chains.Ethereum) ?
            ethBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()) :
            bscBlockNumberLinkPattern.replace(OptionService.PATTERN_BLOCK_NUMBER, model.getHeight().getNumber().toString()))
        .build());
  }

}
