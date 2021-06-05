package com.blockwit.bwf.service.chains.common.utils;

@FunctionalInterface
public interface CheckedFunction<T, R> {
  R apply(T t) throws Throwable;
}
