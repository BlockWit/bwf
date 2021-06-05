package com.blockwit.bwf.form;

import com.blockwit.bwf.controller.Constants;
import lombok.*;

import javax.persistence.Id;
import javax.validation.Constraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateAccountPasswordInternal {

  @Id
  @NotNull
  @Min(0)
  @Max(Long.MAX_VALUE)
  private Long id;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.pwd.regexp.error}")
  private String password;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.repwd.regexp.error}")
  private String repassword;

}
