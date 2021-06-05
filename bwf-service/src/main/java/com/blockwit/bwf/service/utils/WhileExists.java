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
