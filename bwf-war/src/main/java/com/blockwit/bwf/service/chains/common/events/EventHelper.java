package com.blockwit.bwf.service.chains.common.events;

import com.blockwit.bwf.service.chains.common.events.sigs.ChainEventFieldParam;
import com.blockwit.bwf.service.chains.common.events.sigs.ChainEventSignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.protocol.core.methods.response.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EventHelper {

	public static String toString(ChainEventSignature sign, Map<String, String> fields) {
		return sign.getName() + "(" + fields.entrySet().stream()
			.map(e -> e.getKey() + " = " + e.getValue())
			.collect(Collectors.joining(",")) + ")";
	}

	public static Map<String, String> logToFields(ChainEventSignature sign, Log logResult) {
		List<ChainEventFieldParam> indexedFieldParams = sign.getFieldParams().stream().filter(ChainEventFieldParam::isIndexed).collect(Collectors.toList());
		List<ChainEventFieldParam> nonIndexedFieldParams = sign.getFieldParams().stream().filter(t -> t.isIndexed() != true).collect(Collectors.toList());
		List<ChainEventFieldParam> fieldParams = new ArrayList<>();
		fieldParams.addAll(indexedFieldParams);
		fieldParams.addAll(nonIndexedFieldParams);

		final int prefixSize = 2;

		List<String> indexedValuesFromTopics = logResult.getTopics().subList(1, logResult.getTopics().size());

		Map<String, String> fields = new HashMap<>();

		String logData = logResult.getData().substring(2);
		byte[] logDataBytes;
		try {
			logDataBytes = Hex.decodeHex(logData.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
			log.error("Can't decode string. Can't convert bytes string to bytes \"" + logData + "\"");
			fieldParams.stream().forEach(fp -> fields.put(fp.getName(), "ERROR"));
			return fields;
		}

		final int memoryBlockSize = 32;

		final int addressShift = 24;


		for (int i = 0; i < fieldParams.size(); i++) {
			ChainEventFieldParam fieldParam = fieldParams.get(i);
			switch (fieldParam.getType()) {
				case address:
					fields.put(fieldParam.getName(), "0x" + indexedValuesFromTopics.get(i).substring(prefixSize + addressShift));
					break;
				case bytes32:
					// TODO: Check it!
					fields.put(fieldParam.getName(), "0x" + indexedValuesFromTopics.get(i).substring(prefixSize));
					break;
				case uint8:
				case uint256:
					fields.put(fieldParam.getName(), new BigInteger(logDataBytes, (i - indexedValuesFromTopics.size()) * memoryBlockSize, memoryBlockSize).toString());
					break;
				case string:
					int stringSizeOffset = new BigInteger(logDataBytes, (i - indexedValuesFromTopics.size()) * memoryBlockSize, memoryBlockSize).intValue();
					int stringSize = new BigInteger(logDataBytes, stringSizeOffset, memoryBlockSize).intValue();
					int stringOffset = stringSizeOffset + memoryBlockSize;
					byte[] stringBytes = Arrays.copyOfRange(logDataBytes, stringOffset, stringOffset + stringSize);
					String string = null;
					try {
						string = new String(stringBytes, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						log.error("Can't decode string from bytes \"" + stringBytes + "\" from param " + fieldParam.getName() + ". Set empty string!");
					}
					fields.put(fieldParam.getName(), string);
					break;
			}

		}

		return fields;
	}

}
