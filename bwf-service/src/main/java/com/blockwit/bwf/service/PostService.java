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
import com.blockwit.bwf.model.posts.Post;
import com.blockwit.bwf.model.posts.PostStatus;
import com.blockwit.bwf.model.posts.PostType;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.PostRepository;
import com.blockwit.bwf.service.utils.WithOptional;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PostService {

  @Autowired
  PostRepository postRepository;

  @Autowired
  AccountRepository accountRepository;

  public Page<Post> findPostsPageable(PageRequest pageRequest) {
    Page<Post> page = postRepository.findAll(pageRequest);

    List<Post> content = page.getContent();
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

  @Transactional
  public Either<Error, Post> createPost(
      String postTitle,
      String postContent,
      PostType postType,
      PostStatus postStatus,
      String metaTitle,
      String metaDescr,
      String metaKeywords,
      String login) {
    return AccountService.withAccount(accountRepository, login, account ->
        Either.right(
            postRepository.save(
                Post.builder()
                    .content(postContent)
                    .created(System.currentTimeMillis())
                    .metaDescr(metaDescr)
                    .metaKeywords(metaKeywords)
                    .metaTitle(metaTitle)
                    .postType(postType)
                    .owner(account)
                    .ownerId(account.getId())
                    .title(postTitle)
                    .postStatus(postStatus)
                    .build()
            ).toBuilder()
                .owner(account)
                .build()
        )
    );
  }

  public Either<Error, Post> findById(long postId) {
    return ServiceHelper.findOwnableById(
        accountRepository,
        postRepository,
        postId);
  }

  @Transactional
  public Either<Error, Post> updatePost(
      Long id,
      String postTitle,
      String postContent,
      PostType postType,
      PostStatus postStatus,
      String metaTitle,
      String metaDescr,
      String metaKeywords) {
    return WithOptional.process(postRepository.findById(id),
        () -> Either.left(new Error(Error.EC_POST_NOT_FOUND, Error.EM_POST_NOT_FOUND + ": " + id)),
        post ->
            AccountService.withAccount(accountRepository, post.getOwnerId(), account ->
                Either.right(
                    postRepository.save(
                        post.toBuilder()
                            .content(postContent)
                            .metaDescr(metaDescr)
                            .metaKeywords(metaKeywords)
                            .metaTitle(metaTitle)
                            .postType(postType)
                            .title(postTitle)
                            .postStatus(postStatus)
                            .build()
                    ).toBuilder()
                        .owner(account)
                        .build()
                )
            )
    );
  }

  @Transactional
  public Either<Error, Post> deleteById(long postId) {
    return WithOptional.process(postRepository.findById(postId),
        () -> Either.left(new Error(Error.EC_POST_NOT_FOUND, Error.EM_POST_NOT_FOUND + ": " + postId)),
        post -> AccountService.withAccount(accountRepository, post.getOwnerId(), account -> {
              postRepository.deleteById(postId);
              return Either.right(
                  post.toBuilder()
                      .owner(account)
                      .build()
              );
            }
        )
    );
  }

  public List<Post> all() {
    return postRepository.findAll();
  }

  @Transactional
  public List<Post> saveAll(List<Post> post) {
    return postRepository.saveAll(post);
  }

}
