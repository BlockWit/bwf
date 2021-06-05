package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.model.Error;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class WithOptional {

  public static <T, R> R process(Optional<T> r, Supplier<R> errorF, Function<T, R> successF) {
    return r.isPresent() ? successF.apply(r.get()) : errorF.get();
  }

  public static <T, R> R process(Either<Error, T> r, Supplier<R> errorF, Function<T, R> successF) {
    return r.isLeft() ? errorF.get() : successF.apply(r.get());
  }

  public static <T, R> R process(Supplier<Optional<T>> f, Supplier<R> errorF, Function<T, R> successF) {
    return process(f.get(), errorF, successF);
  }

  public static <R> void process(Optional<R> optional, Consumer<R> f) {
    optional.ifPresentOrElse(f::accept, () -> log.error("Optional value " + optional + " is empty!"));
  }

  public static <R> void process(Either<Error, R> either, Consumer<R> f) {
    if (either.isRight()) f.accept(either.get());
  }

  public static <T> Boolean processIsRepeat(Either<Error, T> either, Function<T, Boolean> f) {
    return either.fold(error -> false, r -> f.apply(r));
  }

  public static <T> Boolean processIsRepeat(Optional<T> optional, Function<T, Boolean> f) {
    if (optional.isPresent())
      return f.apply(optional.get());

    log.error("Optional value " + optional + " is empty!");
    return false;
  }

}
