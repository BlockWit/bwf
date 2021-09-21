package com.blockwit.bwf.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPageableService<T> {

	Page<T> findPageable(Pageable pageable);

}
