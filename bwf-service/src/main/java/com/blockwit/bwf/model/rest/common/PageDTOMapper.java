package com.blockwit.bwf.model.rest.common;

import org.springframework.data.domain.Page;

import java.io.Serializable;

public class PageDTOMapper implements Serializable {

	public static final <T> PageDTO<T> map(Page<T> page) {
		return new PageDTO(page.getSize(),
			page.getNumber() + 1,
			page.getTotalPages(),
			page.getTotalElements(),
			page.getContent());
	}

}
