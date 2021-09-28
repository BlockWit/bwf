/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.model;

import com.blockwit.bwf.form.EditOption;
import com.blockwit.bwf.service.OptionService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "options")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Option implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 100, nullable = false, unique = true)
	private String name;

	@Column(length = 100, nullable = false)
	private String type;

	@Column(columnDefinition = "TEXT", length = 100, nullable = false)
	private String value;

	@Column
	private String descr;

	@JsonIgnore
	public Object getPerformedValue() {
		return OptionService.getPerformedValue(this);
	}

	@JsonIgnore
	public <T> T getPerformedValueCast() {
		return (T) OptionService.getPerformedValue(this);
	}

	public static final EditOption editOptionForm(Option option) {
		return new EditOption(option.getId(),
			option.getName(),
			option.getType(),
			option.getValue(),
			option.getDescr());
	}

	public static final Option fromEditOptionForm(EditOption editOption) {
		return new Option(editOption.getId(),
			editOption.getName(),
			editOption.getType(),
			editOption.getValue(),
			editOption.getDescr());
	}

}
