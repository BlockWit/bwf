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

package com.blockwit.bwf.controller;

public class Constants {

	public final static String REGEXP_LOGIN = "^(\\w|\\.){3,50}$";

	public final static String REGEXP_CONFIRM_CODE = "^[a-f0-9]{99}$";

	public final static String REGEXP_PASSWORD = "^(\\w|\\{|\\}|!|#|\\$|%|~|\\^|&|\\*|\\(|\\)|\\\\|\"|'|\\+|-|=|_|\\{|\\}|\\[|\\]|\\/|\\?|>|<|,|\\,|`|\\|:|;){8,50}$";

}
