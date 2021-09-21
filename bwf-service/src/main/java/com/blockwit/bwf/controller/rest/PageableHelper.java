package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.IPageableService;
import com.blockwit.bwf.model.rest.common.PageableDTO;
import com.blockwit.bwf.model.rest.common.PageableDTOMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

public class PageableHelper {

	public static final <T, R> ResponseEntity<PageableDTO<R>> pageable(IPageableService<T> pageableService,
																	   int page,
																	   int pageSize,
																	   Function<T, R> itemsDTOMapper) {

		if (page < 1)
			page = 0;
		else
			page--;

		return ResponseEntity.ok(PageableDTOMapper.map(pageableService
			.findPageable(PageRequest.of(page, pageSize))
			.map(itemsDTOMapper::apply)));
	}

}
