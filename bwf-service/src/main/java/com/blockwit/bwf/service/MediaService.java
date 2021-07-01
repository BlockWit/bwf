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

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.media.Media;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.MediaRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MediaService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private MediaRepository mediaRepository;

  public Either<Error, Media> findById(long postId) {
    return ServiceHelper.findOwnableById(
        accountRepository,
        mediaRepository,
        postId);
  }

  public Page<Media> findAllMediaPageable(PageRequest pageRequest) {
    Page<Media> page = mediaRepository.findAll(pageRequest);

    List<Media> content = page.getContent();
    List<Account> accounts = accountRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getOwnerId())
            .distinct()
            .toList());

    return new PageImpl(StreamEx.of(content)
        .map(notification -> notification.toBuilder()
            .owner(StreamEx.of(accounts).findFirst(t -> t.getId().equals(notification.getOwnerId())).get())
            .build())
        .toList(),
        page.getPageable(),
        page.getTotalElements());
  }

}
