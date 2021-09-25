package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.posts.Post;
import com.blockwit.bwf.model.rest.posts.PostDTO;
import com.blockwit.bwf.model.rest.posts.PostDTOMapper;
import com.blockwit.bwf.service.PostService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for posts")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_POSTS)
public class AdminPostsRestController extends WithListController<Post, PostService, PostDTO> {

	public AdminPostsRestController(PostService modelService) {
		this.modelService = modelService;
		this.mapper = t -> PostDTOMapper.map(t);
	}

}