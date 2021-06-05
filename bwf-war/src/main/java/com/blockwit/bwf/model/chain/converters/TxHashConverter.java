package com.blockwit.bwf.model.chain.converters;

import com.blockwit.bwf.model.chain.factories.TxHashFactory;
import com.blockwit.bwf.model.chain.fields.ITxHash;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TxHashConverter implements AttributeConverter<ITxHash, String> {

	@Override
	public String convertToDatabaseColumn(ITxHash txHash) {
		return txHash == null ? null : txHash.getHash();
	}

	@Override
	public ITxHash convertToEntityAttribute(String s) {
		return s == null ? null : TxHashFactory.create(s);
	}

}
