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

package com.blockwit.bwf.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class WhileExists {

	/**
	 * TODO: refactoring
	 * Use Supplier if it takes nothing, but returns something.
	 * <p>
	 * Use Consumer if it takes something, but returns nothing.
	 * <p>
	 * Use Callable if it returns a result and might throw (most akin to Thunk in general CS terms).
	 * <p>
	 * Use Runnable if it does neither and cannot throw.
	 *
	 * @param consumer
	 */
	public static <T> void process(Supplier<List<T>> supplier,
																 Consumer<List<T>> consumer) {
		List<T> result = supplier.get();
		while (!result.isEmpty()) {
			consumer.accept(result);
			result = supplier.get();
		}
	}

}
