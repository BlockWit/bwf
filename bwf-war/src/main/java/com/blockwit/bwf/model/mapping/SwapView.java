package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SwapView {

  private Long id;

  private String status;

  private String address;

  private String amount;

  private String direction;

  private String startTxHash;

  private String finalizeTxHash;

  private String log;

  private String prevLog;

  private String createTime;

  private String updateTime;


  private String addressLink;

  private String startTxHashLink;

  private String finalizeTxHashLink;

}
