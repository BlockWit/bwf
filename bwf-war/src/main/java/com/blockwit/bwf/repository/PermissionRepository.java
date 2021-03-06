package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Optional<Permission> findByName(String permission);

	Page<Permission> findAll(Pageable pageable);

}
