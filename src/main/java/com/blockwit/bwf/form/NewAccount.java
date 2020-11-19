package com.blockwit.bwf.form;

import com.blockwit.bwf.controller.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAccount {

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = Constants.REGEXP_LOGIN,
            message = "{model.newaccount.login.regexp.error}")
    private String login;

    @NotNull
    @Email(message = "{model.newaccount.email.regexp.error}")
    private String email;

}
