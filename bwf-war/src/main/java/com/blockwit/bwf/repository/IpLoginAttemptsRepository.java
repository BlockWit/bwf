package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpLoginAttemptsRepository extends JpaRepository<LoginAttempts, Long> {

  Optional<LoginAttempts> findFirstByAddr(String addr);

}
