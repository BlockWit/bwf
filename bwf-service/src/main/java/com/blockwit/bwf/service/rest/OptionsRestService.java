package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.config.ConfigDTO;
import com.blockwit.bwf.model.rest.media.ConfigDTOMapper;
import com.blockwit.bwf.model.rest.options.OptionDTO;
import com.blockwit.bwf.model.rest.options.OptionDTOMapper;
import com.blockwit.bwf.service.OptionService;
import org.springframework.stereotype.Component;

import java.util.List;

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

	public ConfigDTO config() {
		return ConfigDTOMapper.map(optionService.findByNameIn(List.of(OptionService.OPTION_HOME_PAGE_ID)));
	}
}
