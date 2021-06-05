package com.blockwit.bwf.service;

import com.blockwit.bwf.model.LoginAttempts;
import com.blockwit.bwf.repository.IpLoginAttemptsRepository;
import com.blockwit.bwf.service.chains.common.utils.WithOptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IpLoginAttemptService {

  @Autowired
  IpLoginAttemptsRepository ipLoginAttemptsRepository;

  @Transactional
  public Optional<LoginAttempts> deleteById(long accountId) {
    return WithOptional.process(ipLoginAttemptsRepository.findById(accountId), () -> Optional.empty(), account -> {
      ipLoginAttemptsRepository.deleteById(accountId);
      return Optional.of(account);
    });
  }

  @Transactional
  public void resetAttempts(String key) {
    ipLoginAttemptsRepository.findFirstByAddr(key).ifPresent(ipLoginAttemptsRepository::delete);
  }

  @Transactional
  public LoginAttempts save(LoginAttempts l) {
    return ipLoginAttemptsRepository.save(l);
  }
}

