package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SwapFinalizeTxLogView implements ITxLogView {

  private Long id;

  private String chain;

  private String tokenAddr;

  private String toAddr;

  private String amount;

  private String createTxHash;

  private String txHash;

  private String blockHash;

  private String blockNumber;

  private String status;

  private String log;

  private String createTime;

  private String updateTime;


  private String tokenAddrLink;

  private String toAddrLink;

  private String createTxHashLink;

  private String txHashLink;

  private String blockHashLink;

  private String blockNumberLink;

}
