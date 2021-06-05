package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SwapPairView {

  private Long id;

  private String status;

  private String bep20Addr;

  private String erc20Addr;

  private String symbol;

  private String name;

  private Integer decimals;

  private String createTime;


  private String bep20AddrLink;

  private String erc20AddrLink;

}
