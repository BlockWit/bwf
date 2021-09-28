package com.blockwit.bwf.controller.rest.front;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.rest.config.ConfigDTO;
import com.blockwit.bwf.service.rest.OptionsRestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Front REST API for config")
@RequestMapping(RestUrls.REST_URL_API_V_1_FRONT_CONFIG)
public class AppConfigRestController {

	@Autowired
	OptionsRestService optionsRestService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ConfigDTO> item() {
		return ResponseEntity.ok(optionsRestService.config());
	}

}
