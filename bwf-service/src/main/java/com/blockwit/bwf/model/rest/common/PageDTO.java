package com.blockwit.bwf.model.rest.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PageDTO<T> implements Serializable {

	private int pageSize;

	private int page;

	private int pagesCount;

	private long totalElements;

	private List<T> items;

}
