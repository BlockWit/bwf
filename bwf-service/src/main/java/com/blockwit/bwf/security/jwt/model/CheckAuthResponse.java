package com.blockwit.bwf.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAuthResponse {

	private Boolean valid;

	private String msg;

}
