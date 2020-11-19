package com.blockwit.bwf.models.service;

import com.blockwit.bwf.models.entity.Post;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class PostService {

    private final PostService postService;

    public PostService(PostService postService) {
        this.postService = postService;
    }

    public Optional<Post> findById(Long id) {
        return postService.findById(id);
    }

    public Post save(String body, String title, Long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        return postService.save(post);
    }
}
