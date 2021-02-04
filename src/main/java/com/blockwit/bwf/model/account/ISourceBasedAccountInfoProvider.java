package com.blockwit.bwf.model.account;

public interface ISourceBasedAccountInfoProvider<T> extends IAccountInfoProvider {

	T getAccountInfoSource();

}
