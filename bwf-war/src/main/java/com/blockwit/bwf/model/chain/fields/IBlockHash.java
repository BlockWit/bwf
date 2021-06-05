package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.chain.IChainContextDepends;

import java.io.Serializable;

public interface IBlockHash extends IChainContextDepends, Serializable {

	String getHash();

}
