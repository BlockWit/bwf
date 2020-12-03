package com.blockwit.bwf.service;

import com.blockwit.bwf.dto.PostDTO;
import com.blockwit.bwf.model.Post;
import com.blockwit.bwf.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class PostService {

	private final ModelMapper modelMapper = new ModelMapper();
	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Page<PostDTO> findAll(int pageNumber, int pageSize, Sort sort) {
		Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort != null ? sort : Sort.by("id").descending());
		return postRepository.findAll(pageRequest).map(post -> modelMapper.map(post, PostDTO.class));
	}

	public Optional<PostDTO> findById(Long id) {
		return postRepository.findById(id).map(post -> modelMapper.map(post, PostDTO.class));
	}

	public PostDTO save(PostDTO postDTO) {
		Post post = postRepository.save(modelMapper.map(postDTO, Post.class));
		return modelMapper.map(post, PostDTO.class);
	}

}
