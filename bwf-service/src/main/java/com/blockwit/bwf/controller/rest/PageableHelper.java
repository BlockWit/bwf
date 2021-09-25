package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.common.PageDTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;

public class PageableHelper {

	public static final String PARAM_PAGE_SIZE = "size";

	public static final String PARAM_PAGE_NUMBER = "page";

	public static final int PAGE_SIZE_LIMIT = 100;

	public static final int PAGE_SIZE_DEFAULT = 10;

	public static final int PAGE_NUMBER_DEFAULT = 1;

	public static final <T, R> PageDTO<R> pageable(Function<Pageable, Page<T>> pageableFetcher,
												   int page,
												   int pageSize,
												   Function<T, R> itemsDTOMapper) {

		if (page < 1)
			page = 0;
		else
			page--;

		if (pageSize > PageableHelper.PAGE_SIZE_LIMIT)
			pageSize = PageableHelper.PAGE_SIZE_LIMIT;
		if (pageSize < 1)
			pageSize = PageableHelper.PAGE_SIZE_DEFAULT;

		return PageDTOMapper.map(
			pageableFetcher.apply(PageRequest.of(page, pageSize))
				.map(itemsDTOMapper::apply));
	}

}
