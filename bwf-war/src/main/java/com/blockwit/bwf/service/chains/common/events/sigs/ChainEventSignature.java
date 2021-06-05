package com.blockwit.bwf.service.chains.common.events.sigs;

import lombok.Getter;
import org.web3j.protocol.core.methods.response.Log;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.web3j.crypto.Hash.sha3;

@Getter
public class ChainEventSignature {

	private String name;
	private List<ChainEventFieldParam> fieldParams;
	private String sign;

	public ChainEventSignature(String name, List<ChainEventFieldParam> fieldParams) {
		this.name = name;
		this.fieldParams = fieldParams;
		this.sign = sha3(DatatypeConverter.printHexBinary(toString().getBytes(StandardCharsets.UTF_8)));
	}

	public boolean logMatch(Log log) {
		return log.getTopics().get(0).equals(sign);
	}

	@Override
	public String toString() {
		return name + "(" + fieldParams.stream()
			.map(ChainEventFieldParam::getType)
			.map(String::valueOf)
			.collect(Collectors.joining(",")) + ")";
	}

}
