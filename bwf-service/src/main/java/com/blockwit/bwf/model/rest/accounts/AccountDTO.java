package com.blockwit.bwf.model.rest.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AccountDTO implements Serializable {

	private Long id;

	private String login;

	private String email;

}