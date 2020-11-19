package com.blockwit.bwf.repository;

import com.blockwit.bwf.entity.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

	Optional<Option> findByName(String login);

	Set<Option> findByNameIn(List<String> names);

	Page<Option> findAll(Pageable pageable);

}
