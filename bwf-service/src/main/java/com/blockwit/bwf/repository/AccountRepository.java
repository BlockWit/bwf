/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Page<Account> findAll(Pageable pageable);

	Optional<Account> findByLogin(String login);

	Optional<Account> findById(Long id);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByLoginAndConfirmCode(String login, String confirmCode);

	Optional<Account> findByLoginAndAndPasswordRecoveryCode(String login, String passwordRecoveryCode);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);

}
