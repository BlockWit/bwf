package com.blockwit.bwf.models.repository;

import com.blockwit.bwf.models.entity.Option;
import com.blockwit.bwf.models.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Page<Role> findAll(Pageable pageable);

}