package com.blockwit.bwf.service.utils;

@FunctionalInterface
public interface CheckedSupplier<T> {
  T call() throws Throwable;
}
