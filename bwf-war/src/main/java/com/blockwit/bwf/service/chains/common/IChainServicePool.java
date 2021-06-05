package com.blockwit.bwf.service.chains.common;

public interface IChainServicePool<T extends IChainService> {

	T borrowObject() throws Exception;

	void returnObject(T chainService);

	boolean isClosed();

}
