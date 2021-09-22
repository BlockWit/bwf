package com.blockwit.bwf.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoleDTO implements Serializable {

	private Long id;

	private String name;

}