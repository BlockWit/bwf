package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.options.OptionDTO;
import com.blockwit.bwf.model.rest.options.OptionDTOMapper;
import com.blockwit.bwf.service.OptionService;
import org.springframework.stereotype.Component;

@Component
public class OptionsRestService {

	OptionService optionService;

	public OptionsRestService(OptionService optionService) {
		this.optionService = optionService;
	}

	public PageDTO<OptionDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> optionService.findPageable(t),
			page,
			pageSize,
			t -> OptionDTOMapper.map(t));
	}

}
