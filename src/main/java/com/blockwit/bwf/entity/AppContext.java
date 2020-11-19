package com.blockwit.bwf.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppContext {

	public static final int MAX_PAGE_SIZE = 100;

	public static final int DEFAULT_PAGE_SIZE = 10;

	String appName;

	String appVersion;

}
