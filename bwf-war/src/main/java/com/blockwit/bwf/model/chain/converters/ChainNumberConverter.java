package com.blockwit.bwf.model.chain.converters;

import com.blockwit.bwf.model.chain.factories.ChainNumberFactory;
import com.blockwit.bwf.model.chain.fields.IChainNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ChainNumberConverter implements AttributeConverter<IChainNumber, String> {

	@Override
	public String convertToDatabaseColumn(IChainNumber chainNumber) {
		return chainNumber == null ? null : chainNumber.toString();
	}

	@Override
	public IChainNumber convertToEntityAttribute(String s) {
		return s == null ? null : ChainNumberFactory.create(s);
	}

}
