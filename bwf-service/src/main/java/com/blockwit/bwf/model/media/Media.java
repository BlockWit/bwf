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

package com.blockwit.bwf.model.media;

import com.blockwit.bwf.model.IOwnable;
import com.blockwit.bwf.model.account.Account;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "media")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Media implements Serializable, IOwnable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Long ownerId;

  @Column(nullable = false)
  private String path;

  @Column(nullable = false)
  private Long created;

  @Column(nullable = false)
  private String mediaType;

  @Column(nullable = false)
  private Boolean pub;

  @Transient
  private Account owner;

}
