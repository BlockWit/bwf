package com.blockwit.bwf.model.rest.options;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OptionDTO implements Serializable {

	private Long id;

	private String name;

	private String type;

	private String value;

	private String description;

}