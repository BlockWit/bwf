package com.blockwit.bwf.model.chain.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@Getter
@Slf4j
public class ChainNumber implements IChainNumber {

  private BigInteger value;

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IChainNumber)
      return ((IChainNumber) obj).getValue().equals(obj);
    return false;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public IChainNumber getCopy() {
    return new ChainNumber(new BigInteger(this.value.toString()));
  }

  @Override
  public String getFormattedString() {
    String result = getFormattedString(18, 18);
    return new BigDecimal(result).stripTrailingZeros().toPlainString();
  }

  @Override
  public String getFormattedString(int decimals, int decLimit) {
    String strValue = value.toString();
    String intPart;
    String decPart;
    if (strValue.length() == decimals) {
      intPart = "0";
      decPart = strValue;
    } else if (strValue.length() > decimals) {
      intPart = strValue.substring(0, strValue.length() - decimals);
      decPart = strValue.substring(strValue.length() - decimals);
    } else {
      intPart = "0";
      decPart = "0".repeat(decimals - strValue.length()) + strValue;
    }

    if (decLimit > decimals) {
      log.warn("Dec limit grater than decimals!");
      decPart = "";
    } else if (decLimit == decimals) {
      //decPart = "";
    } else if (decLimit < decimals && decLimit > 0) {
      if (decLimit < decPart.length())
        decPart = decPart.substring(0, decLimit);
    }

    if (decPart.isEmpty())
      return intPart;

    return intPart + "." + decPart;
  }

}
