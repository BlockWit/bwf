package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.rest.OptionDTO;
import com.blockwit.bwf.model.rest.mappers.OptionDTOMapper;
import com.blockwit.bwf.service.OptionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api("REST API for OPTIONS")
@RequestMapping(RestUrls.REST_URL_API_V_1_OPTIONS)
public class OptionsRestController {

	@Autowired
	OptionService optionService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OptionDTO>> options() {
		return ResponseEntity.ok(optionService.findAll().stream()
			.map(model -> OptionDTOMapper.map(model))
			.collect(Collectors.toList()));
	}

}