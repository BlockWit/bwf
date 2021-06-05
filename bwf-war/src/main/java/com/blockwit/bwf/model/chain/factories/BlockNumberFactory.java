package com.blockwit.bwf.model.chain.factories;

import com.blockwit.bwf.model.chain.fields.BlockNumber;
import com.blockwit.bwf.model.chain.fields.IBlockNumber;

public class BlockNumberFactory {

	public static IBlockNumber create(long blockNumber) {
		return new BlockNumber(blockNumber);
	}

}
