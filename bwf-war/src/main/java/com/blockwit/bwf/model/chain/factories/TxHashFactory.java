package com.blockwit.bwf.model.chain.factories;

import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.model.chain.fields.TxHash;

public class TxHashFactory {

	public static ITxHash create(String str) {
		return new TxHash(str);
	}

}
