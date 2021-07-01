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
import com.blockwit.bwf.service.AppUpdatableInfo;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.PostService;
import com.blockwit.bwf.service.utils.WithOptional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
public class AppController {

  @Autowired
  private AppUpdatableInfo appUpdatableInfo;

  @Autowired
  private PostService postService;

  @Autowired
  private PostViewMapper postViewMapper;

  @Autowired
  private OptionService optionService;

  @GetMapping("/")
  public ModelAndView appHome() {
    return WithOptional.process(optionService.findByName(OptionService.OPTION_HOME_PAGE_ID),
        () -> {
          log.warn("Default home page id not specified");
          ModelAndView model = new ModelAndView("front/pages/home");
          model.addAllObjects(appUpdatableInfo.getInfoMap());
          return model;
        },
        pageIdOption -> {
          Long pageId = (Long) pageIdOption.getPerformedValue();
          return postService.findById(pageId).fold(
              error -> {
                log.warn("Page with id " + pageId + " not found");
                ModelAndView model = new ModelAndView("front/pages/home");
                model.addAllObjects(appUpdatableInfo.getInfoMap());
                return model;
              }
              , post -> AccessContextHelper.access(
                  () -> {
                    log.warn("Not allowed to view post with id " + pageId);
                    ModelAndView model = new ModelAndView("front/pages/home");
                    model.addAllObjects(appUpdatableInfo.getInfoMap());
                    return model;
                  },
                  account -> {
                    if (post.getOwnerId().equals(account.getId())) {
                      return Optional.of(new ModelAndView(
                          "front/pages/" + (post.getPostType().equals(PostType.PAGE) ? "page" : "post"), Map.of("post", postViewMapper.map(post, null).get())));
                    } else {
                      log.warn("Not allowed to view post with id " + pageId);
                      ModelAndView model = new ModelAndView("front/pages/home");
                      model.addAllObjects(appUpdatableInfo.getInfoMap());
                      return Optional.of(model);
                    }
                  },
                  () -> {
                    if (post.getPostStatus().equals(PostStatus.PUBLISHED)) {
                      return new ModelAndView("front/pages/" + (post.getPostType().equals(PostType.PAGE) ? "page" : "post"), Map.of("post", postViewMapper.map(post, null).get()));
                    } else {
                      log.warn("Not allowed to view post with id " + pageId + " because nt published");
                      ModelAndView model = new ModelAndView("front/pages/home");
                      model.addAllObjects(appUpdatableInfo.getInfoMap());
                      return model;
                    }
                  }
              )
          );
        }

    );
  }


  @GetMapping("/panel")
  public String panelHome(Model model) {
    model.addAllAttributes(appUpdatableInfo.getInfoMap());
    return "panel/pages/home";
  }

  @GetMapping("/panel/profile")
  public String panelProfile(Model model) {
    return "panel/pages/profile";
  }

}
