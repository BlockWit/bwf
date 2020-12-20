package com.blockwit.bwf.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column
	private LocalDateTime timestamp;

	@Column
	private Comment parent;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post target;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Account owner;
}
