package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SwapStartTxLogView implements ITxLogView {

	private Long id;

	private String chain;

	private String tokenAddr;

	private String fromAddr;

	private String amount;

	private String feeAmount;

	private String txHash;

	private String blockHash;

	private String blockNumber;

	private String status;

	private String createTime;

	private String updateTime;


	private String tokenAddrLink;

	private String fromAddrLink;

	private String txHashLink;

	private String blockHashLink;

	private String blockNumberLink;


}
