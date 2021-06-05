package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.LoginAttempts;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(loginAttempts)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
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
