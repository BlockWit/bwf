package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.Error;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@AllArgsConstructor
@Getter
public class TxHash implements ITxHash {

	private String hash;

	@Override
	public String toString() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ITxHash)
			return ((ITxHash) obj).getHash().equals(hash);
		return false;
	}

	@Override
	public byte[] getBytes() throws DecoderException {
		return Hex.decodeHex(hash.substring(2));
	}

	public Either<Error, byte[]> getBytesEither() {
		try {
			return Either.right(getBytes());
		} catch (DecoderException e) {
			e.printStackTrace();
			return Either.left(new Error(Error.EC_BYTES_DECODE_EXCEPTION, Error.EM_BYTES_DECODE_EXCEPTION));
		}
	}


	@Override
	public int hashCode() {
		return hash.hashCode();
	}

}
