package com.blockwit.bwf.controllers.model;

import com.blockwit.bwf.controllers.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountPassword {

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = Constants.REGEXP_LOGIN,
            message = "{model.newaccount.login.regexp.error}")
    private String login;

    @NotNull
    @Pattern(regexp = Constants.REGEXP_CONFIRM_CODE,
            message = "{model.newaccount.code.regexp.error}")
    private String code;

    @NotNull
    @Pattern(regexp = Constants.REGEXP_PASSWORD,
            message = "{model.newaccount.pwd.regexp.error}")
    private String password;

    @NotNull
    @Pattern(regexp = Constants.REGEXP_PASSWORD,
            message = "{model.newaccount.repwd.regexp.error}")
    private String repassword;

}
