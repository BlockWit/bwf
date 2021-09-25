package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.media.MediaDTO;
import com.blockwit.bwf.model.rest.media.MediaDTOMapper;
import com.blockwit.bwf.service.MediaService;
import org.springframework.stereotype.Component;

@Component
public class MediaRestService {

	MediaService mediaService;

	public MediaRestService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public PageDTO<MediaDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> mediaService.findPageable(t),
			page,
			pageSize,
			t -> MediaDTOMapper.map(t));
	}

}
