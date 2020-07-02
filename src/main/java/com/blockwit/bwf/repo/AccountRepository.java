package com.blockwit.bwf.repo;

import com.blockwit.bwf.models.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
