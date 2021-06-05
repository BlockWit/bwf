package com.blockwit.bwf.model.chain.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChainAddress implements IChainAddress {

	private String address;

	@Override
	public String toString() {
		return address;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IChainAddress)
			return ((IChainAddress) obj).getAddress().equals(address);
		return false;
	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}

}
