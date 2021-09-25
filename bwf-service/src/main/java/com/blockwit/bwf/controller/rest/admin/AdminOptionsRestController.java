package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.rest.options.OptionDTO;
import com.blockwit.bwf.model.rest.options.OptionDTOMapper;
import com.blockwit.bwf.service.OptionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for options")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_OPTIONS)
public class AdminOptionsRestController extends WithListController<Option, OptionService, OptionDTO> {

	public AdminOptionsRestController(OptionService modelService) {
		this.modelService = modelService;
		this.mapper = t -> OptionDTOMapper.map(t);
	}

}