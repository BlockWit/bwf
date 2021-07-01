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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MigrTest {

  public static void main(String[] args) {
    Pattern pattern = Pattern.compile("src=\\\"\\/app\\/media\\/(.*?)\\\"");

    String start = "src=\"/app/media/";
    String end = "\"";

      Matcher matcher = pattern.matcher("shjgdg src=\"/app/media/cromlehg/20191004/1570203826373.jpg\"\n" +
          "djkd src=\"/app/media/cromlehg/20191004/1570203826373.jpg\" djkld src=\"/app/media/cromlehg/20191004/15702038123326373.jpg\" djkd");
      while(matcher.find()) {
        String matched = matcher.group();
        String relPath = matched.substring(start.length(), matched.length()-1);

        System.out.println(relPath);
      }

   }


}
