package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.media.Media;
import com.blockwit.bwf.model.rest.MediaDTO;

public class MediaDTOMapper {

	public static MediaDTO map(Media model) {
		return new MediaDTO(model.getId(),
			model.getOwnerId(),
			model.getPath(),
			model.getCreated(),
			model.getMediaType(),
			model.getPub(),
			AccountDTOMapper.map(model.getOwner()));
	}

}