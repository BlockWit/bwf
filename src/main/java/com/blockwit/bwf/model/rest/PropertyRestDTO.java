package com.blockwit.bwf.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyRestDTO {
	private Long id;
	private String name;
	private String description;
	private String propertyType;
	private String value;
}
