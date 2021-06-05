package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TxView {

  private Long id;

  private String chain;

  private String fromAddr;

  private String toAddr;

  private String amount;

  private String gas;

  private String gasPrice;

  private String txHash;

  private String blockHash;

  private String blockNumber;

  private String nonce;

  private String status;

  private String createTime;

  private String updateTime;


  private String fromAddrLink;

  private String toAddrLink;

  private String txHashLink;

  private String blockHashLink;

  private String blockNumberLink;


}
