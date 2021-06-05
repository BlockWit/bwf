package com.blockwit.bwf.model.chain.converters;

import com.blockwit.bwf.model.chain.factories.BlockNumberFactory;
import com.blockwit.bwf.model.chain.fields.IBlockNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BlockNumberConverter implements AttributeConverter<IBlockNumber, Long> {

	@Override
	public Long convertToDatabaseColumn(IBlockNumber blockNumber) {
		return blockNumber == null ? null : blockNumber.getNumber();
	}

	@Override
	public IBlockNumber convertToEntityAttribute(Long s) {
		return s == null ? null : BlockNumberFactory.create(s);
	}

}
