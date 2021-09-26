package com.blockwit.bwf.controller.rest.front;

import com.blockwit.bwf.controller.AccessContextHelper;
import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.posts.PostStatus;
import com.blockwit.bwf.model.rest.posts.PostDTO;
import com.blockwit.bwf.service.rest.PostsRestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@Api("Front REST API for posts")
@RequestMapping(RestUrls.REST_URL_API_V_1_FRONT_POSTS)
public class RestPostsController {

	@Autowired
	PostsRestService postsRestService;

	@GetMapping(path = RestUrls.REST_URL_API_V_1_POSTS_REL_POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PostDTO> item(@PathVariable("postId") Long postId) {
		return postsRestService.findPostById(postId).fold(
			error -> ResponseEntity.notFound().build(),
			post -> AccessContextHelper.access(
				() -> ResponseEntity.ok(post),
				account -> {
					if (post.getOwnerId().equals(account.getId())) {
						return Optional.of(ResponseEntity.ok(post));
					}
					return Optional.empty();
				},
				() -> {
					if (post.getPostStatus().equals(PostStatus.PUBLISHED)) {
						return ResponseEntity.ok(post);
					}
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
			)
		);
	}

}