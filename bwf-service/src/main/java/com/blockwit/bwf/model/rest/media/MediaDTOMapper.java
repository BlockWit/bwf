package com.blockwit.bwf.model.rest.media;

import com.blockwit.bwf.model.media.Media;
import com.blockwit.bwf.model.rest.accounts.AccountDTOMapper;
import com.blockwit.bwf.model.rest.media.MediaDTO;

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