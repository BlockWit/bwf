package com.blockwit.bwf.dto;

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

	private Long id;

	@NotBlank(message = "Post body must not be empty")
	private String body;

	private PostStatus status;

	@NotBlank(message = "Post title must not be empty")
	@Size(max = 256, message = "Post title must be up to 256 characters long")
	private String title;

	private Account owner;

}
