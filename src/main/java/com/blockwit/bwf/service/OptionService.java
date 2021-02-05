package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OptionService {

	public final static String OPTION_APP_NAME = "APP_NAME";
	public final static String OPTION_APP_SHORT_NAME = "APP_SHORT_NAME";
	public final static String OPTION_APP_VERSION = "APP_VERSION";
	public final static String OPTION_APP_YEAR = "APP_YEAR";
	public final static String OPTION_TYPE_STRING = "string";

	private final OptionRepository optionRepository;
	private final Map<String, Option> defaultOptions;
	private final List<String> defaultOptionNames;

	public OptionService(OptionRepository optionRepository) {
		this.optionRepository = optionRepository;
		defaultOptions = Map.of(
			OPTION_APP_NAME, new Option(0L, OPTION_APP_NAME, OPTION_TYPE_STRING, "BWF", "Application full name"),
			OPTION_APP_VERSION, new Option(0L, OPTION_APP_VERSION, OPTION_TYPE_STRING, "next", "Application version"),
			OPTION_APP_SHORT_NAME, new Option(0L, OPTION_APP_SHORT_NAME, OPTION_TYPE_STRING, "BWF", "Application short name"),
			OPTION_APP_YEAR, new Option(0L, OPTION_APP_YEAR, OPTION_TYPE_STRING, "2021", "Application year")
		);

		defaultOptionNames = defaultOptions.keySet().stream().collect(Collectors.toList());
	}

	private Option getOrCreateDefaultOption(String name) {
		return optionRepository.findByName(name).orElseGet(() -> {
			Option option = defaultOptions.get(name);
			return optionRepository.save(option);
		});
	}

	public Map<String, String> getAllDefaultValues() {
		Map<String, String> namesToValues = optionRepository.findByNameIn(defaultOptionNames)
			.stream().collect(Collectors.toMap(Option::getName, Option::getValue));

		if (!namesToValues.keySet().containsAll(defaultOptionNames)) {
			for (String name : defaultOptionNames) {
				if (!namesToValues.containsKey(name)) {
					optionRepository.save(defaultOptions.get(name));
				}
			}

			namesToValues = optionRepository.findByNameIn(defaultOptionNames)
				.stream().collect(Collectors.toMap(Option::getName, Option::getValue));
		}

		return namesToValues;
	}

}
