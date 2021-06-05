package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.chain.IChainContextDepends;

import java.io.Serializable;

public interface IBlockNumber extends IChainContextDepends, Serializable {

	Long getNumber();

}
