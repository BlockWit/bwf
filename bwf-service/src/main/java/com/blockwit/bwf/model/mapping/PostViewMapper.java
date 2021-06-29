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

import com.blockwit.bwf.model.posts.Post;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostViewMapper implements IMapper<Post, PostView> {

  @Override
  public List<PostView> map(List<Post> posts, Object context) {
    return StreamEx.of(posts)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<PostView> map(Post post, Object context) {
    return fromModelToView(post);
  }

  private static Optional<PostView> fromModelToView(Post model) {
    return Optional.of(PostView.builder()
        .id(model.getId())
        .ownerId(model.getOwnerId())
        .title(model.getTitle())
        .thumbnail(model.getThumbnail())
        .content(model.getContent())
        .postStatus(model.getPostStatus())
        .created(model.getCreated() == null ? "" : dateTimeFormatter.print(model.getCreated()))
        .metaTitle(model.getMetaTitle())
        .metaDescr(model.getMetaDescr())
        .metaKeywords(model.getMetaKeywords())
        .postType(model.getPostType())
        .ownerName(model.getOwner().getLogin())
        .build());
  }

}
