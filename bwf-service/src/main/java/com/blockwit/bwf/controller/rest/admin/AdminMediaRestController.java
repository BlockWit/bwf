package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.media.MediaDTO;
import com.blockwit.bwf.service.rest.MediaRestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for media")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_MEDIA)
public class AdminMediaRestController {

	@Autowired
	MediaRestService mediaRestService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<MediaDTO>> items(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return ResponseEntity.ok(mediaRestService.findAll(page, pageSize));
	}

}