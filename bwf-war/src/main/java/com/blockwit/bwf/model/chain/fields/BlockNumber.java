package com.blockwit.bwf.model.chain.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BlockNumber implements IBlockNumber {

	private Long number;

	@Override
	public String toString() {
		return number + "";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IBlockNumber)
			return ((IBlockNumber) obj).getNumber().equals(number);
		return false;
	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}

}
