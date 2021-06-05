package com.blockwit.bwf.service.chains.common.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChainServicePoolMetrics {

	private int maxTotal;

	private int numActive;

	private int numIdle;

}
