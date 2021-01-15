package com.blockwit.bwf.dto;

import com.blockwit.bwf.model.Account;
import com.blockwit.bwf.model.Comment;
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
public class CommentDTO {
	private Long id;

	@NotBlank(message = "Post body must not be empty")
	private String body;

	private Account owner;

	private CommentDTO parent;

	private PostDTO destination;
}
