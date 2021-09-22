package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.posts.Post;
import com.blockwit.bwf.model.rest.PostDTO;

public class PostDTOMapper {

	public static PostDTO map(Post model) {
		return new PostDTO(model.getId(),
			model.getOwnerId(),
			model.getTitle(),
			model.getThumbnail(),
			model.getContent(),
			model.getPostStatus(),
			model.getCreated(),
			model.getMetaTitle(),
			model.getMetaDescr(),
			model.getMetaKeywords(),
			model.getPostType(),
			AccountDTOMapper.map(model.getOwner()));
	}

}