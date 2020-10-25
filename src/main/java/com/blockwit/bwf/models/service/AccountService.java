package com.blockwit.bwf.models.service;

import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.entity.ConfirmationStatus;
import com.blockwit.bwf.models.repository.AccountRepository;
import com.blockwit.bwf.models.service.exceptions.*;
import com.blockwit.bwf.services.PasswordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final RoleService roleService;

    private final PasswordService passwordService;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          RoleService roleService,
                          PasswordService passwordService,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.passwordService = passwordService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Account> findByLoginAndConfirmCode(String login, String confirmCode) {
        return accountRepository.findByLoginAndConfirmCode(login, confirmCode);
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

        Account account = new Account();
        account.setLogin(login);
        account.setEmail(email);
        account.setRoles(new HashSet<>(List.of(accountRepository.count() == 0 ? roleService.getDefaultAdminRole() : roleService.getDefaultUserRole())));
        account.setConfirmationStatus(ConfirmationStatus.WAITE_SENDING_VERIFICATION_TOKEN);
        account.setConfirmCode(passwordService.generateRegistrationToken(login));

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

    public Account _tryLogin(String login, String password) throws
            NotFoundAccountServiceException,
            WrongConfirmStatusAccountServiceException,
            WaitConfirmationAccountServiceException,
            WrongCredentialsAccountServiceException {

        Optional<Account> accountOpt = accountRepository.findByLogin(login.toLowerCase());
        if (accountOpt.isEmpty())
            throw new NotFoundAccountServiceException(login);

        Account account = accountOpt.get();
        if (account.getConfirmationStatus() == ConfirmationStatus.WAIT_CONFIRMATION)
            throw new WaitConfirmationAccountServiceException(login);

        if(account.getConfirmationStatus() != ConfirmationStatus.CONFIRMED)
            throw new WrongConfirmStatusAccountServiceException(account.getConfirmationStatus(), ConfirmationStatus.WAIT_CONFIRMATION);

        //passwordEncoder.matches()
        return null;
    }
}
