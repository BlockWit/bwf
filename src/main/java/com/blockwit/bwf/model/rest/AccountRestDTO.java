package com.blockwit.bwf.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountRestDTO {
	private Long id;
	private String login;
	private String email;
	private String status;
}
