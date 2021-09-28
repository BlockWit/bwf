package com.blockwit.bwf.model.menu;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menu")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MenuItem implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Long parentId;

	@Column(nullable = false)
	private Long menuId;

	@Column
	private String link;

	@Column
	private String name;

	@Column
	private int itemOrder;

	@Column(nullable = false)
	private Long created;


	@Transient
	private List<MenuItem> children;

	@Transient
	private MenuItem parent;


}