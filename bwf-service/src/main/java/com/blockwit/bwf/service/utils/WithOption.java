package com.blockwit.bwf.service.utils;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.service.OptionService;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class WithOption {

	public static void process(OptionService optionService, String optionName, Consumer<Option> f) {
		optionService.findByName(optionName).ifPresentOrElse(f::accept, () -> log.error("Option " + optionName + " not found!"));
	}

}
