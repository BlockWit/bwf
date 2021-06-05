package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IpLoginAttemptView {

  private Long id;

  private String addr;

  private String lastBadAttempt;

  private Integer badAttemptsCount;

}
