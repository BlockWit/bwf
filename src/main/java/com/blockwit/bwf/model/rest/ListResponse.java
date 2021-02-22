package com.blockwit.bwf.model.rest;

import java.util.List;

public class ListResponse<T> {

	private List<T> content;

	public ListResponse(List<T> content) {
		this.content = content;
	}

	public List<T> getContent() {
		return content;
	}

	public int getTotalElements() {
		return content.size();
	}

}
