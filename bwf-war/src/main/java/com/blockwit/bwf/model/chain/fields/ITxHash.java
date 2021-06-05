package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.chain.IChainContextDepends;
import io.vavr.control.Either;
import org.apache.commons.codec.DecoderException;

import java.io.Serializable;

public interface ITxHash extends IChainContextDepends, Serializable {

	String getHash();

	byte[] getBytes() throws DecoderException;

	Either<Error, byte[]> getBytesEither();

}
