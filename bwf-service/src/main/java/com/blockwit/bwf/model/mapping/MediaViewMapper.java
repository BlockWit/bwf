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

package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.media.Media;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MediaViewMapper implements IMapper<Media, MediaView> {

  @Override
  public List<MediaView> map(List<Media> medias, Object context) {
    return StreamEx.of(medias)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<MediaView> map(Media media, Object context) {
    return fromModelToView(media);
  }

  private static Optional<MediaView> fromModelToView(Media model) {
    return Optional.of(MediaView.builder()
        .id(model.getId())
        .ownerId(model.getOwnerId())
        .created(model.getCreated() == null ? "" : dateTimeFormatter.print(model.getCreated()))
        .mediaType(model.getMediaType())
        .pub(model.getPub())
        .path(model.getPath())
        .ownerName(model.getOwner().getLogin())
        .build());
  }

}
