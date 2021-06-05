package com.blockwit.bwf.service.chains.gasproviders;

import java.math.BigInteger;

public interface IGasProvider {

	BigInteger getGasPrice();

	BigInteger getGasLimit();

}
