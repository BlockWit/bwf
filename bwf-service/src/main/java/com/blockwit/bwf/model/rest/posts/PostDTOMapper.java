package com.blockwit.bwf.model.rest.posts;

import com.blockwit.bwf.model.posts.Post;
import com.blockwit.bwf.model.rest.accounts.AccountDTOMapper;
import com.blockwit.bwf.model.rest.posts.PostDTO;

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