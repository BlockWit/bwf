package com.blockwit.bwf.service.chains.common.utils;

@FunctionalInterface
public interface CheckedSupplier<T> {
  T call() throws Throwable;
}
