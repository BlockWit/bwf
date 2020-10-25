package com.blockwit.bwf.models.repository;

import com.blockwit.bwf.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByLoginAndConfirmCode(String login, String confirmCode);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

}