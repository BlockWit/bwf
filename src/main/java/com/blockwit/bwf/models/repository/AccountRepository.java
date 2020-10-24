package com.blockwit.bwf.models.repository;

import com.blockwit.bwf.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

}