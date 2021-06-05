package com.blockwit.bwf.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditOption {

	@Id
	@NotNull
	@Min(0)
	@Max(Long.MAX_VALUE)
	private Long id;

	@Size(min = 1, max = 100)
	@NotNull
	private String name;

	@Size(min = 1, max = 100)
	@NotNull
	private String type;

	@Size(max = 100)
	@NotNull
	private String value;

	@Size(max = 250)
	private String descr;

}
