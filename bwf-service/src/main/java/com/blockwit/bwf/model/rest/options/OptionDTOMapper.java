package com.blockwit.bwf.model.rest.options;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.options.OptionDTO;

public class OptionDTOMapper {

	public static OptionDTO map(Option model) {
		return new OptionDTO(model.getId(),
			model.getName(),
			model.getType(),
			model.getValue(),
			model.getDescr());
	}

}