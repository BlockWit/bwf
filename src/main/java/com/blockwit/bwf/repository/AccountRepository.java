package com.blockwit.bwf.repository;

import com.blockwit.bwf.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findAll(Pageable pageable);

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByLoginAndConfirmCode(String login, String confirmCode);

    Optional<Account> findByLoginAndAndPasswordRecoveryCode(String login, String passwordRecoveryCode);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

}
