package com.blockwit.bwf.service.chains;

import com.blockwit.bwf.model.chain.factories.ChainNumberFactory;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ChainsInfo {

  public static final String BSC_HEIGHT = "BSC_HEIGHT";

  public static final String ETH_HEIGHT = "ETH_HEIGHT";

  public static final String BSC_FREE_TOKENS = "BSC_FREE_TOKENS";

  public static final String ETH_FREE_TOKENS = "ETH_FREE_TOKENS";

  public static final String BSC_ALL_TOKENS = "BSC_ALL_TOKENS";

  public static final String ETH_ALL_TOKENS = "ETH_ALL_TOKENS";

  public static final String BSC_SYNC_PROGRESS = "BSC_SYNC_PROGRESS";

  public static final String ETH_SYNC_PROGRESS = "ETH_SYNC_PROGRESS";

  public static final String BSC_INVOKER_BALANCE = "BSC_INVOKER_BALANCE";

  public static final String ETH_INVOKER_BALANCE = "ETH_INVOKER_BALANCE";

  private IChainNumber bscFreeTokens = ChainNumberFactory.create("0");

  private IChainNumber ethFreeTokens = ChainNumberFactory.create("0");

  private IChainNumber bscAllTokens = ChainNumberFactory.create("0");

  private IChainNumber ethAllTokens = ChainNumberFactory.create("0");

  private Long bscHeight = 0L;

  private Long ethHeight = 0L;

  private IChainNumber bscInvokerBalance = ChainNumberFactory.create("0");

  private IChainNumber ethInvokerBalance = ChainNumberFactory.create("0");

  private Integer bscSyncProgress = 0;

  private Integer ethSyncProgress = 0;

  public synchronized Map<String, Object> getInfoMap() {
    return Map.of(BSC_FREE_TOKENS, bscFreeTokens.getCopy(),
        ETH_FREE_TOKENS, ethFreeTokens.getCopy(),
        BSC_ALL_TOKENS, bscAllTokens.getCopy(),
        ETH_ALL_TOKENS, ethAllTokens.getCopy(),
        BSC_INVOKER_BALANCE, bscInvokerBalance.getCopy(),
        ETH_INVOKER_BALANCE, ethInvokerBalance.getCopy(),
        BSC_HEIGHT, bscHeight,
        ETH_HEIGHT, ethHeight,
        BSC_SYNC_PROGRESS, bscSyncProgress,
        ETH_SYNC_PROGRESS, ethSyncProgress);
  }

  public synchronized void setBscFreeTokens(IChainNumber bscFreeTokens) {
    this.bscFreeTokens = bscFreeTokens;
  }

  public synchronized void setEthFreeTokens(IChainNumber ethFreeTokens) {
    this.ethFreeTokens = ethFreeTokens;
  }

  public synchronized void setBscAllTokens(IChainNumber bscAllTokens) {
    this.bscAllTokens = bscAllTokens;
  }

  public synchronized void setEthAllTokens(IChainNumber ethAllTokens) {
    this.ethAllTokens = ethAllTokens;
  }

  public synchronized void setBscHeight(Long bscHeight) {
    this.bscHeight = bscHeight;
  }

  public synchronized void setEthHeight(Long ethHeight) {
    this.ethHeight = ethHeight;
  }

  public synchronized void setBscInvokerBalance(IChainNumber bscInvokerBalance) {
    this.bscInvokerBalance = bscInvokerBalance;
  }

  public synchronized void setEthInvokerBalance(IChainNumber ethInvokerBalance) {
    this.ethInvokerBalance = ethInvokerBalance;
  }

  public synchronized void setBscSyncProgress(Integer bscSyncProgress) {
    this.bscSyncProgress = bscSyncProgress;
  }

  public synchronized void setEthSyncProgress(Integer ethSyncProgress) {
    this.ethSyncProgress = ethSyncProgress;
  }

  public synchronized IChainNumber getBscFreeTokens() {
    return bscFreeTokens.getCopy();
  }

  public synchronized IChainNumber getEthFreeTokens() {
    return ethFreeTokens.getCopy();
  }

  public synchronized IChainNumber getBscAllTokens() {
    return bscAllTokens.getCopy();
  }

  public synchronized IChainNumber getEthAllTokens() {
    return ethAllTokens.getCopy();
  }

  public synchronized Long getBscHeight() {
    return bscHeight;
  }

  public synchronized Long getEthHeight() {
    return ethHeight;
  }

  public synchronized IChainNumber getBscInvokerBalance() {
    return bscInvokerBalance.getCopy();
  }

  public synchronized IChainNumber getEthInvokerBalance() {
    return ethInvokerBalance.getCopy();
  }

  public synchronized Integer getBscSyncProgress() {
    return bscSyncProgress;
  }

  public Integer getEthSyncProgress() {
    return ethSyncProgress;
  }
}
