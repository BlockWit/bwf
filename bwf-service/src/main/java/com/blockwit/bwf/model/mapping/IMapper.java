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

package com.blockwit.bwf.model.mapping;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

public interface IMapper<FROM, TO> {

  public static final String datetimePattern = "yyyy-MM-dd HH:mm:ss";

  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yy HH:mm:ss");

  Optional<TO> map(FROM from, Object context);

  List<TO> map(List<FROM> from, Object context);

}
