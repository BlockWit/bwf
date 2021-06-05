package com.blockwit.bwf.model.chain.converters;

import com.blockwit.bwf.model.chain.factories.ChainAddressFactory;
import com.blockwit.bwf.model.chain.fields.IChainAddress;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ChainAddressConverter implements AttributeConverter<IChainAddress, String> {

	@Override
	public String convertToDatabaseColumn(IChainAddress chainAddress) {
		return chainAddress == null ? null : chainAddress.getAddress();
	}

	@Override
	public IChainAddress convertToEntityAttribute(String s) {
		return s == null ? null : ChainAddressFactory.create(s);
	}

}
