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

package com.blockwit.bwf.controller.web;

import com.blockwit.bwf.controller.ControllerHelper;
import com.blockwit.bwf.form.EditPost;
import com.blockwit.bwf.form.NewPost;
import com.blockwit.bwf.model.mapping.PostViewMapper;
import com.blockwit.bwf.service.PostService;
import com.blockwit.bwf.service.utils.WithOptional;
import com.blockwit.bwf.validator.EditPostValidator;
import com.blockwit.bwf.validator.NewPostValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/posts")
public class PostsController {

  private static final String TP_POST_NEW = "panel/pages/posts/create-post";

  private static final String TP_POST_EDIT = "panel/pages/posts/edit-post";

  @Autowired
  private PostService postService;

  @Autowired
  private PostViewMapper postViewMapper;

  @Autowired
  private NewPostValidator newPostValidator;

  @Autowired
  private EditPostValidator editPostValidator;

  @GetMapping
  public ModelAndView appPanelPosts() {
    return new ModelAndView("redirect:/panel/posts/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelPosts(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/posts/posts"),
        pageNumber,
        pageRequest -> postService.findPostsPageable(pageRequest),
        postViewMapper);
  }


  @GetMapping("/create")
  public ModelAndView createPostGET() {
    return new ModelAndView(TP_POST_NEW,
        Map.of("newPost", new NewPost()));
  }

  @PostMapping("/create")
  public ModelAndView createPostPOST(
      RedirectAttributes redirectAttributes,
      @ModelAttribute("newPost") @Valid NewPost newPost,
      BindingResult bindingResult
  ) {
    log.debug("Perform new post form checks");

    newPostValidator.validate(newPost, bindingResult);
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView(TP_POST_NEW, bindingResult.getModel(), HttpStatus.BAD_REQUEST);
      return modelAndView;
    }

    log.info("Create post");
    return postService.createPost(
        newPost.getPostTitle(),
        newPost.getPostContent(),
        newPost.getPostType(),
        newPost.getPostStatus(),
        newPost.getMetaTitle(),
        newPost.getMetaDescr(),
        newPost.getMetaKeywords(),
        SecurityContextHolder.getContext().getAuthentication().getName()).fold(
        error -> {
          ModelAndView modelAndView = new ModelAndView(TP_POST_NEW, HttpStatus.BAD_REQUEST);
          modelAndView.addObject("message_error", error.getDescr());
          return modelAndView;
        }
        , post -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/posts/page/1",
            "Post " + post.getTitle() + " successfully created with id " + post.getId())
    );
  }


  @PostMapping("/post/{postId}/edit")
  public ModelAndView editPostPOST(
      RedirectAttributes redirectAttributes,
      @PathVariable long postId,
      @ModelAttribute("editPost") @Valid EditPost editPost,
      BindingResult bindingResult
  ) {
    log.info("Perform edit post form checks");

    editPostValidator.validate(editPost, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView(TP_POST_EDIT, bindingResult.getModel(), HttpStatus.BAD_REQUEST);


    if (postId != editPost.getId().longValue())
      return ControllerHelper.returnError400(bindingResult,
          TP_POST_EDIT, "Sanity checks not passed - id from url and from form must be equals");

    log.info("Update post");
    return postService.updatePost(
        editPost.getId(),
        editPost.getPostTitle(),
        editPost.getPostContent(),
        editPost.getPostType(),
        editPost.getPostStatus(),
        editPost.getMetaTitle(),
        editPost.getMetaDescr(),
        editPost.getMetaKeywords()
    ).fold(
        error -> ControllerHelper.returnError400(bindingResult, TP_POST_EDIT, error.getDescr())
        , post -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/posts/page/1",
            "Post " + post.getTitle() + " successfully updated with id " + post.getId())
    );
  }

  @GetMapping(value = "/post/{postId}/edit")
  public ModelAndView editPostGET(HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  @PathVariable long postId) {
    return WithOptional.process(postService.findById(postId),
        () -> ControllerHelper.returnError400(request, redirectAttributes, "Can't edit post with id " + postId + "! May be not found?")
        , post ->
            new ModelAndView(TP_POST_EDIT, Map.of("editPost",
                EditPost.builder()
                    .id(post.getId())
                    .postTitle(post.getTitle())
                    .postContent(post.getContent())
                    .postType(post.getPostType())
                    .postStatus(post.getPostStatus())
                    .metaDescr(post.getMetaDescr())
                    .metaTitle(post.getMetaTitle())
                    .metaKeywords(post.getMetaKeywords())
                    .build()))
    );
  }

  @PostMapping("/post/{postId}/delete")
  public ModelAndView deletePostPOST(
      RedirectAttributes redirectAttributes,
      @PathVariable long postId,
      BindingResult bindingResult
  ) {
    return postService.deleteById(postId).fold(
        error -> ControllerHelper.returnError400(bindingResult, "panel/pages/posts/posts", error.getDescr())
        , post -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/posts/page/1",
            "Post " + post.getTitle() + " successfully delete with id " + post.getId())
    );
  }

}
