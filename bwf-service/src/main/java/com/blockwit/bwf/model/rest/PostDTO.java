package com.blockwit.bwf.model.rest;

import com.blockwit.bwf.model.posts.PostStatus;
import com.blockwit.bwf.model.posts.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PostDTO implements Serializable {

	private Long id;

	private Long ownerId;

	private String title;

	private String thumbnail;

	private String content;

	private PostStatus postStatus;

	private Long created;

	private String metaTitle;

	private String metaDescr;

	private String metaKeywords;

	private PostType postType;

	private AccountDTO owner;

}