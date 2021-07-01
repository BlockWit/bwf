/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.PostViewMapper;
import com.blockwit.bwf.model.posts.PostStatus;
import com.blockwit.bwf.model.posts.PostType;
import com.blockwit.bwf.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/posts")
public class AppPostsController {

  @Autowired
  private PostService postService;

  @Autowired
  private PostViewMapper postViewMapper;

  @GetMapping("/post/{postId}")
  public ModelAndView viewPostPOST(
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      @PathVariable long postId
  ) {
    return viewPostHelper(
        postService,
        postViewMapper,
        request,
        redirectAttributes,
        postId
    );
  }

  public static ModelAndView viewPostHelper(
      PostService postService,
      PostViewMapper postViewMapper,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      long postId
  ) {
    return postService.findById(postId).fold(
        error -> ControllerHelper.returnError404(request, redirectAttributes, error.getDescr())
        , post -> AccessContextHelper.access(
            () -> new ModelAndView("front/pages/" + (post.getPostType().equals(PostType.PAGE) ? "page" : "post"), Map.of("post", postViewMapper.map(post, null).get())),
            account -> {
              if (post.getOwnerId().equals(account.getId())) {
                return Optional.of(new ModelAndView(
                    "front/pages/" + (post.getPostType().equals(PostType.PAGE) ? "page" : "post"), Map.of("post", postViewMapper.map(post, null).get())));
              } else
                return Optional.empty();
            },
            () -> {
              if (post.getPostStatus().equals(PostStatus.PUBLISHED)) {
                return new ModelAndView("front/pages/" + (post.getPostType().equals(PostType.PAGE) ? "page" : "post"), Map.of("post", postViewMapper.map(post, null).get()));
              } else {
                return ControllerHelper.returnError400(request, redirectAttributes, "Can't view post with id " + postId);
              }
            }
        )
    );
  }


}
