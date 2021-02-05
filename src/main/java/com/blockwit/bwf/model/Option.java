package com.blockwit.bwf.model;

import com.blockwit.bwf.form.EditOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "options")
@AllArgsConstructor
@NoArgsConstructor
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
