package com.blockwit.bwf.models.service;

import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.entity.ConfirmationStatus;
import com.blockwit.bwf.models.repository.AccountRepository;
import com.blockwit.bwf.models.service.exceptions.DefaultAdminRoleNotExistsServiceException;
import com.blockwit.bwf.models.service.exceptions.DefaultUserRoleNotExistsServiceException;
import com.blockwit.bwf.models.service.exceptions.EmailBusyAccountServiceException;
import com.blockwit.bwf.models.service.exceptions.LoginBusyAccountServiceException;
import com.blockwit.bwf.services.PasswordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final RoleService roleService;

    private final PasswordService passwordService;

    public AccountService(AccountRepository accountRepository, RoleService roleService, PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.passwordService = passwordService;
    }

    @Transactional
    public Account _setConfirmationStatusTokenSended(Account acount) {
        acount.setConfirmationStatus(ConfirmationStatus.WAIT_CONFIRMATION);
        return accountRepository.save(acount);
    }

    /**
     * @param login - not null, checked, without spaces
     * @param email - not null, checked, not null checked lowercase
     * @throws LoginBusyAccountServiceException
     */
    @Transactional
    public Account _registerUnconfirmedAccount(String inLogin, String inEmail) throws
            EmailBusyAccountServiceException,
            LoginBusyAccountServiceException,
            DefaultAdminRoleNotExistsServiceException,
            DefaultUserRoleNotExistsServiceException {

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

        if (accountRepository.count() == 0) {
            account.getRoles().add(roleService.getDefaultAdminRole());
        } else {
            account.getRoles().add(roleService.getDefaultUserRole());
        }

        account.setConfirmationStatus(ConfirmationStatus.WAITE_SENDING_VERIFICATION_TOKEN);
        account.setConfirmCode(passwordService.generateRegistrationToken(login));
        return accountRepository.save(account);
    }

    public boolean isExistsLogin(String login) {
        return accountRepository.existsByLogin(login.trim().toLowerCase());
    }

    public boolean isExistsEmail(String email) {
        return accountRepository.existsByEmail(email.trim().toLowerCase());
    }

}
