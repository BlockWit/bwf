package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.IPageableService;
import com.blockwit.bwf.model.rest.common.PageDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.function.Function;

public class WithListController<T, S extends IPageableService, R> {

	S modelService;

	Function<T, R> mapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<R>> items(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return PageableHelper.pageable(modelService, page, pageSize, mapper);
	}

}
