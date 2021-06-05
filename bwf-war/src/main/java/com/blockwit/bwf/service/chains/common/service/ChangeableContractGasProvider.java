package com.blockwit.bwf.service.chains.common.service;

import com.blockwit.bwf.service.chains.gasproviders.IGasProvider;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class ChangeableContractGasProvider implements ContractGasProvider {

	private IGasProvider gasProvider;

	public void setGasProvider(IGasProvider gasProvider) {
		this.gasProvider = gasProvider;
	}

	@Override
	public BigInteger getGasPrice(String contractFunc) {
		return gasProvider.getGasPrice();
	}

	@Override
	public BigInteger getGasPrice() {
		return gasProvider.getGasPrice();
	}

	@Override
	public BigInteger getGasLimit(String contractFunc) {
		return gasProvider.getGasLimit();
	}

	@Override
	public BigInteger getGasLimit() {
		return gasProvider.getGasLimit();
	}

}
