package com.blockwit.bwf.model.chain.factories;

import com.blockwit.bwf.model.chain.fields.ChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainAddress;

public class ChainAddressFactory {

	public static IChainAddress create(String str) {
		return new ChainAddress(str);
	}

}
