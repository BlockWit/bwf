package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.posts.PostDTO;
import com.blockwit.bwf.model.rest.posts.PostDTOMapper;
import com.blockwit.bwf.service.PostService;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class PostsRestService {

	PostService postsService;

	public PostsRestService(PostService postsService) {
		this.postsService = postsService;
	}

	public PageDTO<PostDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> postsService.findPageable(t),
			page,
			pageSize,
			t -> PostDTOMapper.map(t));
	}

	public Either<Error, PostDTO> findPostById(Long postId) {
		return postsService.findById(postId).map(PostDTOMapper::map);
	}

}
