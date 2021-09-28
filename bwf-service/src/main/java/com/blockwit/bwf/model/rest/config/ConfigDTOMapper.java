package com.blockwit.bwf.model.rest.media;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.config.ConfigDTO;
import com.blockwit.bwf.service.OptionService;

import java.util.Optional;
import java.util.Set;

public class ConfigDTOMapper {

	public static ConfigDTO map(Set<Option> model) {
		Optional<Long> mainPageID = model
			.stream()
			.filter(t -> t.getName().equals(OptionService.OPTION_HOME_PAGE_ID))
			.findFirst()
			.map(Option::getPerformedValueCast);

		return new ConfigDTO(mainPageID.orElse(null));
	}

}