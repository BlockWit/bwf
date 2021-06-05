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

package com.blockwit.bwf.service;

import com.blockwit.bwf.exception.*;
import com.blockwit.bwf.model.ConfirmationStatus;
import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.utils.WithOptional;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class AccountService {

  private final AccountRepository accountRepository;

  private final RoleService roleService;

  private final PasswordService passwordService;

  private final PasswordEncoder passwordEncoder;

  private final RandomService randomService;

  public AccountService(
      AccountRepository accountRepository,
      RoleService roleService,
      PasswordService passwordService,
      PasswordEncoder passwordEncoder,
      RandomService randomService
  ) {
    this.accountRepository = accountRepository;
    this.roleService = roleService;
    this.passwordService = passwordService;
    this.passwordEncoder = passwordEncoder;
    this.randomService = randomService;
  }

  public Optional<Account> findById(long accountId) {
    return accountRepository.findById(accountId);
  }

  public Optional<Account> findByLoginAndConfirmCode(String login, String confirmCode) {
    return accountRepository.findByLoginAndConfirmCode(login, confirmCode);
  }

  public Optional<Account> findByLoginAndAndPasswordRecoveryCode(String login, String confirmCode) {
    return accountRepository.findByLoginAndAndPasswordRecoveryCode(login, confirmCode);
  }

  public Optional<Account> findByEmailOrLogin(String loginOrEmail) {
    String performedLoginOrEmail = loginOrEmail.trim().toLowerCase();
    return accountRepository.findByLogin(performedLoginOrEmail)
        .or(() -> accountRepository.findByEmail(performedLoginOrEmail));
  }

  @Transactional
  public Account _setConfirmationStatusTokenSended(Account account) {
    account.setConfirmationStatus(ConfirmationStatus.WAIT_CONFIRMATION);
    return accountRepository.save(account);
  }

  /**
   * @param inLogin - not null, checked, without spaces
   * @param inEmail - not null, checked, not null checked lowercase
   * @throws LoginBusyAccountServiceException
   */
  @Transactional
  public Account _registerUnconfirmedAccount(String inLogin, String inEmail) throws
      EmailBusyAccountServiceException,
      LoginBusyAccountServiceException {

    String login = inLogin.trim().toLowerCase();
    String email = inEmail.trim().toLowerCase();

    if (accountRepository.existsByLogin(login)) {
      log.trace("Login: " + login + " already exists!");
      throw new LoginBusyAccountServiceException(login);
    }

    if (accountRepository.existsByEmail(email)) {
      log.trace("Email: " + email + " already exists!");
      throw new EmailBusyAccountServiceException(login);
    }

    return accountRepository.save(Account.builder()
        .login(login)
        .email(email)
        .roles(new HashSet<>(List.of(accountRepository.count() == 0 ? roleService.getDefaultAdminRole() : roleService.getDefaultUserRole())))
        .confirmationStatus(ConfirmationStatus.WAITE_SENDING_VERIFICATION_TOKEN)
        .confirmCode(passwordService.generateRegistrationToken(login))
        .build());
  }

  @Transactional
  public Account _setAccountForgottenNewPassword(String login, String password) throws
      NotFoundAccountServiceException,
      WrongConfirmStatusAccountServiceException {

    Optional<Account> accountOpt = accountRepository.findByLogin(login.toLowerCase());
    if (accountOpt.isEmpty())
      throw new NotFoundAccountServiceException(login);

    Account account = accountOpt.get();
    if (account.getConfirmationStatus() != ConfirmationStatus.CONFIRMED)
      throw new WrongConfirmStatusAccountServiceException(account.getConfirmationStatus(), ConfirmationStatus.CONFIRMED);

    account.setPasswordRecoveryCode(null);
    account.setPasswordRecoveryTimestamp(null);
    account.setHash(passwordEncoder.encode(password));

    return accountRepository.save(account);
  }

  @Transactional
  public Account _setAccountConfirmedWithPassword(String login, String password) throws
      NotFoundAccountServiceException,
      WrongConfirmStatusAccountServiceException {

    Optional<Account> accountOpt = accountRepository.findByLogin(login.toLowerCase());
    if (accountOpt.isEmpty())
      throw new NotFoundAccountServiceException(login);

    Account account = accountOpt.get();
    if (account.getConfirmationStatus() != ConfirmationStatus.WAIT_CONFIRMATION)
      throw new WrongConfirmStatusAccountServiceException(account.getConfirmationStatus(), ConfirmationStatus.WAIT_CONFIRMATION);

    account.setConfirmCode(null);
    account.setConfirmationStatus(ConfirmationStatus.CONFIRMED);
    account.setHash(passwordEncoder.encode(password));

    return accountRepository.save(account);
  }

  @Transactional
  public Optional<Account> _generatePasswordRecoveryCode(String loginOrEmail) throws AttemptTimelimitAccountServiceException {
    String preparedLogin = loginOrEmail.trim().toLowerCase();
    Optional<Account> accountOpt = findByEmailOrLogin(preparedLogin);

    if (accountOpt.isPresent()) {

      Account account = accountOpt.get();

      if (account.getPasswordRecoveryTimestamp() != null) {
        long limit = account.getPasswordRecoveryTimestamp() + 60000 - System.currentTimeMillis();
        if (limit > 0)
          throw new AttemptTimelimitAccountServiceException(account.getLogin(), limit);
      }

      account.setPasswordRecoveryCode(generateRecoveryCode(preparedLogin));
      account.setPasswordRecoveryTimestamp(null);
      return Optional.of(accountRepository.save(account));
    }

    return accountOpt;
  }

  public Account _recoveryTokenSended(Account account) {
    account.setPasswordRecoveryTimestamp(System.currentTimeMillis());
    return accountRepository.save(account);
  }

  public String generateRecoveryCode(String login) {
    return Hex.encodeHexString(BCrypt.hashpw(randomService.nextString5() + login + System.currentTimeMillis(), BCrypt.gensalt())
        .replaceAll("\\.", "s")
        .replaceAll("\\\\", "d")
        .replaceAll("\\$", "g")
        .getBytes()).substring(0, 99);
  }

  @Transactional
  public Optional<Account> deleteById(long accountId) {
    return WithOptional.process(accountRepository.findById(accountId), () -> Optional.empty(), account -> {
      accountRepository.deleteById(accountId);
      return Optional.of(account);
    });
  }

  @Transactional
  public Either<Error, Account> updateAccountPasswordInternal(long accountId, String password) {
    return WithOptional.process(accountRepository.findById(accountId),
        () -> Either.left(new Error(Error.EC_ACCOUNT_NO_FOUND, Error.EM_ACCOUNT_NO_FOUND + accountId)), account ->
            WithOptional.process(Optional.ofNullable(accountRepository.save(
                account.toBuilder().hash(passwordEncoder.encode(password)).build())),
                () -> Either.left(new Error(Error.EC_CAN_NOT_UPDATE_ACCOUNT_PASSWORD, Error.EM_CAN_NOT_UPDATE_ACCOUNT + accountId)),
                Either::right)
    );
  }

  @Transactional
  public Either<Error, Account> updateAccountInternal(long accountId, String inLogin, String inEmail) {
    return WithOptional.process(accountRepository.findById(accountId),
        () -> Either.left(new Error(Error.EC_ACCOUNT_NO_FOUND, Error.EM_ACCOUNT_NO_FOUND + accountId)), account -> {
          String login = inLogin.trim().toLowerCase();
          String email = inEmail.trim().toLowerCase();

          Optional<Account> accountOptional = accountRepository.findByLogin(login);
          if (accountOptional.isPresent() && !accountOptional.get().getId().equals(accountId)) {
            log.trace("Login: " + login + " already exists!");
            return Either.left(new Error(Error.EC_LOGIN_EXISTS, Error.EM_LOGIN_EXISTS + login));
          }

          accountOptional = accountRepository.findByEmail(email);
          if (accountOptional.isPresent() && !accountOptional.get().getId().equals(accountId)) {
            log.trace("Email: " + email + " already exists!");
            return Either.left(new Error(Error.EC_EMAIL_EXISTS, Error.EM_EMAIL_EXISTS + email));
          }

          return WithOptional.process(Optional.ofNullable(accountRepository.save(
              account.toBuilder()
                  .login(login)
                  .email(email).build())),
              () -> Either.left(new Error(Error.EC_CAN_NOT_UPDATE_ACCOUNT, Error.EM_CAN_NOT_UPDATE_ACCOUNT + login)),
              Either::right);

        });
  }

  /**
   * @param inLogin - not null, checked, without spaces
   * @param inEmail - not null, checked, not null checked lowercase
   * @throws LoginBusyAccountServiceException
   */
  @Transactional
  public Either<Error, Account> createAccountInternal(String inLogin,
                                                      String inEmail,
                                                      String password) {

    String login = inLogin.trim().toLowerCase();
    String email = inEmail.trim().toLowerCase();

    if (accountRepository.existsByLogin(login)) {
      log.trace("Login: " + login + " already exists!");
      return Either.left(new Error(Error.EC_LOGIN_EXISTS, Error.EM_LOGIN_EXISTS + login));
    }

    if (accountRepository.existsByEmail(email)) {
      log.trace("Email: " + email + " already exists!");
      return Either.left(new Error(Error.EC_EMAIL_EXISTS, Error.EM_EMAIL_EXISTS + email));
    }

    return WithOptional.process(Optional.ofNullable(accountRepository.save(
        Account.builder()
            .login(login)
            .email(email)
            .roles(Set.of(roleService.getDefaultAdminRole()))
            .confirmationStatus(ConfirmationStatus.CONFIRMED)
            .hash(passwordEncoder.encode(password)).build())),
        () -> Either.left(new Error(Error.EC_CAN_NOT_CREATE_ACCOUNT, Error.EM_CAN_NOT_CREATE_ACCOUNT + login)),
        Either::right);

  }

}
