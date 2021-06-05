package com.blockwit.bwf.service.utils;

@FunctionalInterface
public interface CheckedFunction<T, R> {
  R apply(T t) throws Throwable;
}
