package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAll(Pageable pageable);

	Optional<Post> findById(Long id);

}
