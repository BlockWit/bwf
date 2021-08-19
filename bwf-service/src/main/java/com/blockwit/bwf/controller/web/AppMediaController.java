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
import com.blockwit.bwf.controller.MediaHelper;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.MediaRepository;
import com.blockwit.bwf.service.MediaService;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.utils.WithOptional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/media")
public class AppMediaController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  MediaService mediaService;

  @Autowired
  OptionService optionService;

  @Autowired
  MediaRepository mediaRepository;

  @GetMapping(value = "/{mediaId}")
  public ResponseEntity getMedia(
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      @PathVariable Long mediaId) {
    return ControllerHelper.responseEntityMediaWithOwnableSec(
        accountRepository,
        mediaRepository,
        mediaId,
        request,
        redirectAttributes,
        media ->
            WithOptional.process(
                optionService.findByName(OptionService.OPTION_MEDIA_PATH),
                () -> ControllerHelper.internalErrorResponseEntity("Media path not specified in options!"),
                option ->
                    WithOptional.process(
                        MediaHelper.getMediaType(media.getMediaType()),
                        () -> ControllerHelper.internalErrorResponseEntity(
                            "Can't determine media extension for media type " +
                                media.getMediaType() + " for media id " + media.getId()),
                        mediaType -> {
                          String pathToMedia = option.getValue() + "/" + media.getPath();
                          log.trace("Send media " + pathToMedia);
                          return ResponseEntity.ok()
                              .contentType(mediaType)
                              .body(new FileSystemResource(pathToMedia));
                        })
            )
    );
  }

}
