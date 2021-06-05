package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.chain.IChainContextDepends;

import java.io.Serializable;
import java.math.BigInteger;

public interface IChainNumber extends IChainContextDepends, Serializable {

  BigInteger getValue();

  IChainNumber getCopy();

  String getFormattedString(int decimals, int decLimit);

  String getFormattedString();

}
