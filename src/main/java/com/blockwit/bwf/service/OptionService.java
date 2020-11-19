package com.blockwit.bwf.service;

import com.blockwit.bwf.entity.Option;
import com.blockwit.bwf.repository.OptionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OptionService {

    public final static String OPTION_APP_NAME = "APP_NAME";

    public final static String OPTION_APP_VERSION = "APP_VERSION";


    public final static String OPTION_TYPE_STRING = "string";


    private final OptionRepository optionRepository;

    private final Map<String, Option> defaultOptions;

    private final List<String> defaultOptionNames;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
        defaultOptions = Map.of(
                OPTION_APP_NAME, new Option(0L, OPTION_APP_NAME, OPTION_TYPE_STRING, "BWF", "Application name"),
                OPTION_APP_VERSION, new Option(0L, OPTION_APP_VERSION, OPTION_TYPE_STRING, "next", "Application version"));

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
