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

import org.springframework.http.MediaType;

import java.util.Optional;

public class MediaHelper {

  public static Optional<String> getExtension(String mediaType) {
    if (mediaType.equals(MediaType.IMAGE_JPEG_VALUE))
      return Optional.of("jpg");
    if (mediaType.equals(MediaType.IMAGE_GIF_VALUE))
      return Optional.of("gif");
    if (mediaType.equals(MediaType.IMAGE_PNG_VALUE))
      return Optional.of("png");
    return Optional.empty();
  }

  /**
   * TODO: should check access for concrete media type in options
   *
   * @param mediaType
   * @return
   */
  public static Optional<MediaType> getMediaType(String mediaType) {
    if (mediaType.equals(MediaType.IMAGE_JPEG_VALUE))
      return Optional.of(MediaType.IMAGE_JPEG);
    if (mediaType.equals(MediaType.IMAGE_GIF_VALUE))
      return Optional.of(MediaType.IMAGE_GIF);
    if (mediaType.equals(MediaType.IMAGE_PNG_VALUE))
      return Optional.of(MediaType.IMAGE_PNG);
    return Optional.empty();
  }

}
