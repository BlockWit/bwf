package com.blockwit.bwf.model.rest.common;

import org.springframework.data.domain.Page;

import java.io.Serializable;

public class PageableDTOMapper implements Serializable {

	public static final <T> PageableDTO<T> map(Page<T> page) {
		return new PageableDTO(page.getSize(), page.getNumber(), page.getTotalPages(), page.getContent());
	}

}
