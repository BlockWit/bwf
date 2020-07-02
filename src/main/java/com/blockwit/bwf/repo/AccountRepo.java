package com.blockwit.bwf.repo;

import com.blockwit.bwf.models.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {

    public Account findByLogin(String login);

}
