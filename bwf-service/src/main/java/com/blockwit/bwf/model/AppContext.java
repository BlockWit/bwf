/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppContext {

	public static final int MAX_PAGE_SIZE = 100;

	public static final int DEFAULT_PAGE_SIZE = 10;

	String appName;

	String appVersion;

	String appYear;

	String appShortName;

}
