package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.LoginAttempts;
import com.blockwit.bwf.service.OptionService;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class IpLoginAttemptViewMapper implements IMapper<LoginAttempts, IpLoginAttemptView> {

  private final OptionService optionService;

  public IpLoginAttemptViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<IpLoginAttemptView> map(List<LoginAttempts> loginAttempts, Object context) {
    return StreamEx.of(loginAttempts)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<IpLoginAttemptView> map(LoginAttempts loginAttempt, Object context) {
    return fromModelToView(loginAttempt);
  }

  private static Optional<IpLoginAttemptView> fromModelToView(LoginAttempts model) {
    return Optional.of(IpLoginAttemptView.builder()
        .id(model.getId())
        .addr(model.getAddr())
        .badAttemptsCount(model.getBadAttemptsCount())
        .lastBadAttempt(model.getLastBadAttempt() == null ? "" : dateTimeFormatter.print(model.getLastBadAttempt()))
        .build());
  }

}
