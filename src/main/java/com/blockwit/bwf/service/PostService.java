package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Account;
import com.blockwit.bwf.model.Post;
import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.expression.AccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(AccountRepository accountRepository, PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Optional<Post> findById(Long id) {
		return postRepository.findById(id);
	}

	public Post create(String body, String title, Long ownerId) {
		Post post = new Post();
		post.setBody(body);
		post.setTitle(title);
		Account owner = new Account();
		owner.setId(ownerId);
		post.setOwner(owner);
		return postRepository.save(post);
	}

	public Post update(Long id, String body, String title, Long ownerId) {
		Post post = postRepository.findById(id).orElseThrow();
		if (!post.getOwner().getId().equals(ownerId)) throw new AccessDeniedException("You have no permission to perform this action");
		post.setBody(body);
		post.setTitle(title);
		return postRepository.save(post);
	}
}
