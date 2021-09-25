package com.blockwit.bwf.model.rest.media;

import com.blockwit.bwf.model.rest.accounts.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MediaDTO implements Serializable {

	private Long id;

	private Long ownerId;

	private String path;

	private Long created;

	private String mediaType;

	private Boolean pub;

	private AccountDTO owner;

}