package com.blockwit.bwf.model.chain.converters;

import com.blockwit.bwf.model.chain.factories.BlockHashFactory;
import com.blockwit.bwf.model.chain.fields.IBlockHash;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BlockHashConverter implements AttributeConverter<IBlockHash, String> {

	@Override
	public String convertToDatabaseColumn(IBlockHash blockHash) {
		return blockHash == null ? null : blockHash.getHash();
	}

	@Override
	public IBlockHash convertToEntityAttribute(String s) {
		return s == null ? null : BlockHashFactory.create(s);
	}

}
