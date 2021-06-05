package com.blockwit.bwf.model.chain.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BlockHash implements IBlockHash {

	private String hash;

	@Override
	public String toString() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IBlockHash)
			return ((IBlockHash) obj).getHash().equals(hash);
		return false;
	}

	@Override
	public int hashCode() {
		return hash.hashCode();
	}

}
