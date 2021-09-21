package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.OptionDTO;

import java.io.Serializable;

public class OptionDTOMapper implements Serializable {

	public static OptionDTO map(Option model) {
		return new OptionDTO(model.getId(),
			model.getName(),
			model.getType(),
			model.getValue(),
			model.getDescr());
	}

}