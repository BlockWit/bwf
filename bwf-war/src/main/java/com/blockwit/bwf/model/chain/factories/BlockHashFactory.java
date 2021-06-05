package com.blockwit.bwf.model.chain.factories;

import com.blockwit.bwf.model.chain.fields.BlockHash;
import com.blockwit.bwf.model.chain.fields.IBlockHash;

public class BlockHashFactory {

	public static IBlockHash create(String str) {
		return new BlockHash(str);
	}

}
