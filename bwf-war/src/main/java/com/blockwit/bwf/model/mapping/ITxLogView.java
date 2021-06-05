package com.blockwit.bwf.model.mapping;

public interface ITxLogView {

	Long getId();

	String getTxHash();

	String getBlockHash();

	String getBlockNumber();

	String getUpdateTime();

	String getCreateTime();

}
