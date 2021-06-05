package com.blockwit.bwf.form;

import com.blockwit.bwf.controller.Constants;
import lombok.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EditAccountInternal {

  @Id
  @NotNull
  @Min(0)
  @Max(Long.MAX_VALUE)
  private Long id;

  @NotNull
  @Size(min = 3, max = 50)
  @Pattern(regexp = Constants.REGEXP_LOGIN, message = "{model.newaccount.login.regexp.error}")
  private String login;

  @NotNull
  @Email(message = "{model.newaccount.email.regexp.error}")
  private String email;

}
