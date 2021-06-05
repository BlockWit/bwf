package com.blockwit.bwf.model.chain.factories;

import com.blockwit.bwf.model.chain.fields.ChainNumber;
import com.blockwit.bwf.model.chain.fields.IChainNumber;

import java.math.BigInteger;

public class ChainNumberFactory {

	public static IChainNumber create(String str) {
		return new ChainNumber(new BigInteger(str));
	}

}
