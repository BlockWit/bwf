package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.media.Media;
import com.blockwit.bwf.model.rest.media.MediaDTO;
import com.blockwit.bwf.model.rest.media.MediaDTOMapper;
import com.blockwit.bwf.service.MediaService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for media")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_MEDIA)
public class AdminMediaRestController extends WithListController<Media, MediaService, MediaDTO> {

	public AdminMediaRestController(MediaService modelService) {
		this.modelService = modelService;
		this.mapper = t -> MediaDTOMapper.map(t);
	}

}