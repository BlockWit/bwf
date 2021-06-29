/*
 * Copyright (c) 2017-present BlockWit, LLC. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 *
 */

package com.blockwit.bwf.model.posts;

import com.blockwit.bwf.model.account.Account;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Post implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Long ownerId;

  @Column(nullable = false)
  private String title;

  @Column
  private String thumbnail;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private PostStatus postStatus;

  @Column(nullable = false)
  private Long created;

  @Column
  private String metaTitle;

  @Column
  private String metaDescr;

  @Column
  private String metaKeywords;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private PostType postType;

  @Transient
  private Account owner;

}
