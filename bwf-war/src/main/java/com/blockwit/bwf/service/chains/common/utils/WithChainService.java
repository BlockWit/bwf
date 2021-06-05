package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.service.chains.common.IChainService;
import com.blockwit.bwf.service.chains.common.IChainServicePool;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class WithChainService {

  private IChainServicePool chainServicePool;

  public static void process(IChainServicePool chainServicePool, Consumer<IChainService> f, Runnable elseF) {
    if (chainServicePool.isClosed()) {
      log.error("Chain service pool already closed!");
      elseF.run();
      return;
    }

    IChainService chainService;
    try {
      chainService = chainServicePool.borrowObject();
    } catch (Exception e) {
      log.error("Chain service pool borrow object failed", e);
      e.printStackTrace();
      elseF.run();
      return;
    }

    try {
      f.accept(chainService);
    } catch (Exception e) {
      log.error("Error during execute operation with eth service", e);
      e.printStackTrace();
      elseF.run();
      return;
    } finally {
      chainServicePool.returnObject(chainService);
    }
  }

  public static void process(IChainServicePool chainServicePool, Consumer<IChainService> f) {
    if (chainServicePool.isClosed()) {
      log.error("Chain service pool already closed!");
      return;
    }

    IChainService chainService;
    try {
      chainService = chainServicePool.borrowObject();
    } catch (Exception e) {
      log.error("Chain service pool borrow object failed", e);
      e.printStackTrace();
      return;
    }

    try {
      f.accept(chainService);
    } catch (Exception e) {
      log.error("Error during execute operation with eth service", e);
      e.printStackTrace();
      return;
    } finally {
      chainServicePool.returnObject(chainService);
    }
  }

  public interface TwoChainsConsumerF<T> {
    T process(IChainService chainService1, IChainService chainService2);
  }

  public interface TwoChainsConsumerRunnable<T> {
    void process(IChainService chainService1, IChainService chainService2);
  }

  public static <R> R process2ChainsF(IChainServicePool chainServicePool1,
                                      IChainServicePool chainServicePool2,
                                      TwoChainsConsumerF<R> successF,
                                      Supplier<R> errorF) {
    return processF(chainServicePool1,
        chainService1 -> processF(chainServicePool2,
            chainService2 -> successF.process(chainService1, chainService2),
            errorF),
        errorF);
  }

  //  public static void process2ChainsRunnable(IChainServicePool chainServicePool1,
//                                     IChainServicePool chainServicePool2,
//                                     TwoChainsConsumerRunnable successF,
//                                     Runnable errorF) {
//    return processRunnable(chainServicePool1,
//        chainService1 -> processRunnable(chainServicePool2,
//            chainService2 -> successF.process(chainService1, chainService2),
//            errorF),
//        errorF);
//  }
//
  public static <R> R processF(IChainServicePool chainServicePool, Function<IChainService, R> successF, Supplier<R> errorF) {
    if (chainServicePool.isClosed()) {
      log.error("Chain service pool already closed!");
      return errorF.get();
    }

    IChainService chainService;
    try {
      chainService = chainServicePool.borrowObject();
    } catch (Exception e) {
      log.error("Chain service pool borrow object failed", e);
      e.printStackTrace();
      return errorF.get();
    }

    try {
      return successF.apply(chainService);
    } catch (Exception e) {
      log.error("Error during execute operation with eth service", e);
      e.printStackTrace();
      return errorF.get();
    } finally {
      chainServicePool.returnObject(chainService);
    }
  }
//
//  public static void processRunnable(IChainServicePool chainServicePool, Consumer<IChainService> successF, Runnable errorF) {
//    if (chainServicePool.isClosed()) {
//      log.error("Chain service pool already closed!");
//      errorF.run();
//      return;
//    }
//
//    IChainService chainService;
//    try {
//      chainService = chainServicePool.borrowObject();
//    } catch (Exception e) {
//      log.error("Chain service pool borrow object failed", e);
//      e.printStackTrace();
//      errorF.run();
//      return;
//    }
//
//    try {
//      successF.accept(chainService);
//      return;
//    } catch (Exception e) {
//      log.error("Error during execute operation with eth service", e);
//      e.printStackTrace();
//      errorF.run();
//      return;
//    } finally {
//      chainServicePool.returnObject(chainService);
//    }
//  }


}
