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

import com.blockwit.bwf.model.posts.PostStatus;
import com.blockwit.bwf.model.posts.PostType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostView {

  private Long id;

  private Long ownerId;

  private String title;

  private String thumbnail;

  private String content;

  private PostStatus postStatus;

  private String created;

  private String metaTitle;

  private String metaDescr;

  private String metaKeywords;

  private PostType postType;

  private String ownerName;

}
