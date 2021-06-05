package com.blockwit.bwf.service.chains.common.service;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.chain.factories.ChainNumberFactory;
import com.blockwit.bwf.model.chain.factories.TxHashFactory;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.IChainService;
import com.blockwit.bwf.service.chains.common.events.EventInfo;
import com.blockwit.bwf.service.chains.common.events.IEventFactory;
import com.blockwit.bwf.service.chains.common.utils.CheckedFunction;
import com.blockwit.bwf.service.chains.common.utils.CheckedSupplier;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import com.blockwit.bwf.service.chains.gasproviders.IGasProvider;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolutionException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.*;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.ClientConnectionException;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class ChainService implements IChainService {

  public static final String FUNC_FINALIZE_SWAP = "finalizeSwap";

  private Chains chain;

  private Web3j web3j;

  private IEventFactory eventFactory;

  private Credentials credentials;

  private SwapContract swapContract;

  private Either<Error, ERC20Contract> erc20;

  private ChangeableContractGasProvider contractGasProvider = new ChangeableContractGasProvider();

  private TransactionManager transactionManager;

  private IChainAddress swapContractAddress;

  private OptionService optionService;

  public ChainService(Chains chain,
                      Web3j web3j,
                      OptionService optionService,
                      IEventFactory eventFactory,
                      IChainAddress contractAddress,
                      String privateKey,
                      IGasProvider gasProvider) {
    this.chain = chain;
    this.web3j = web3j;
    this.eventFactory = eventFactory;
    this.optionService = optionService;
    this.contractGasProvider.setGasProvider(gasProvider);
    this.swapContractAddress = contractAddress;
    this.credentials = Credentials.create(privateKey);
    this.transactionManager = new FastRawTransactionManager(web3j, credentials);
    this.swapContract = new SwapContract(web3j, contractAddress.getAddress(), transactionManager, contractGasProvider);
  }

  @Override
  public Either<Error, IChainNumber> getInvokerBalance() {
    return withBalance(credentials.getAddress(), balance -> Either.right(ChainNumberFactory.create(balance + "")));
  }

  private Either<Error, ERC20Contract> getERC20Contract() {
    if (erc20 == null) {

      RemoteFunctionCall<String> remoteFunctionCall;
      try {
        remoteFunctionCall = swapContract.token();
      } catch (EnsResolutionException e) {
        e.printStackTrace();
        log.error("Error during call contract function ", e);
        return Either.left(new Error(Error.EC_ENS_RESOLUTION_EXCEPTION, Error.EM_ENS_RESOLUTION_EXCEPTION + ": " + e.getMessage()));
      }

      erc20 = tryEthCallCheckedFinal(() -> remoteFunctionCall.sendAsync().get())
          .map(tokenAddrString ->
              new ERC20Contract(web3j, tokenAddrString, transactionManager, contractGasProvider)
          );

    }
    return erc20;
  }

  @Override
  public Either<Error, Long> getHeight() {
    return tryEthCallChecked(() -> {
      EthBlockNumber result = web3j.ethBlockNumber().sendAsync().get();

      Response.Error error = result.getError();
      if (error != null) {
        log.error("Error with code " + error.getCode() + " and error data " + error.getData() + " during get eth height: ", error.getMessage());
        return Either.left(new Error(Error.EC_GET_BLOCK_NUMBER_ERROR,
            Error.EM_GET_BLOCK_NUMBER_ERROR + " : " +
                "Error with code " + error.getCode() + " and error data " + error.getData() + " during get eth height: " + error.getMessage()));
      }

      return Either.right(result.getBlockNumber().longValue());
    });
  }

  @Override
  public Either<Error, Boolean> isFinalizedSwap(ITxHash startTxHash) {
    return startTxHash.getBytesEither().flatMap(startTxBytes -> {

      RemoteFunctionCall<Boolean> remoteFunctionCall;

      try {
        remoteFunctionCall = swapContract
            .finalizedTxs(startTxBytes);
      } catch (EnsResolutionException e) {
        e.printStackTrace();
        log.error("Error during call remove contract function ", e);
        return Either.left(new Error(Error.EC_ENS_RESOLUTION_EXCEPTION, Error.EM_ENS_RESOLUTION_EXCEPTION + ": " + e.getMessage()));
      }

      return tryEthCallCheckedFinal(() -> remoteFunctionCall.sendAsync().get());
    });
  }

  private <R> Either<Error, R> withBalance(String address, Function<BigInteger, Either<Error, R>> f) {
    return tryEthCall(() -> {
      try {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return f.apply(ethGetBalance.getBalance());
      } catch (IOException e) {
        e.printStackTrace();
        log.error("Can't get balance for " + address + " for " + chain);
        return Either.left(new Error(Error.EC_CAN_NOT_GET_BALANCE,
            Error.EC_CAN_NOT_GET_BALANCE + " for " + address + " in " + chain + " : " + e.getMessage()));
      }
    });
  }


  private <R> Either<Error, R> checkBalancesAndLimits(Supplier<Either<Error, R>> f) {
    List<String> optionNames = List.of(chain + OptionService.OPTION_Chain_BALANCE_LIMIT,
        chain + OptionService.OPTION_Chain_GAS_PRICE_LIMIT);

    return WithOptionsProfiledResult.process(optionService, optionNames,
        options -> {
          BigInteger balanceLimit = (BigInteger) options.get(chain + OptionService.OPTION_Chain_BALANCE_LIMIT).getPerformedValue();
          BigInteger gasPriceStopLimit = (BigInteger) options.get(chain + OptionService.OPTION_Chain_GAS_PRICE_LIMIT).getPerformedValue();
          BigInteger gasPrice = contractGasProvider.getGasPrice();
          if (gasPriceStopLimit.compareTo(gasPrice) == 1)
            return withBalance(credentials.getAddress(), balance -> {
              if (balance.compareTo(balanceLimit) == 1)
                return f.get();
              else {
                log.warn("Balance limit " + balanceLimit + " greater than balance " + balance + " for " + credentials.getAddress() + " in " + chain);
                return Either.left(new Error(Error.EC_BALANCE_LIMIT_EXCEEDED, Error.EM_BALANCE_LIMIT_EXCEEDED + ": limit " + balanceLimit
                    + " greater than account balance " + balance + " for " + credentials.getAddress() + " in " + chain));
              }
            });
          else {
            log.warn("Gas price " + gasPrice + " greater than gas price limit " + gasPriceStopLimit);
            return Either.left(new Error(Error.EC_GAS_PRICES_LIMIT_EXCEEDED, Error.EM_GAS_PRICES_LIMIT_EXCEEDED + gasPrice + " > " + gasPriceStopLimit));
          }
        }, () -> {
          log.error("Can't get options for" + String.join(", ", optionNames));
          return Either.left(new Error(Error.EC_CAN_NOT_GET_OPTIONS, Error.EM_CAN_NOT_GET_OPTIONS + String.join(", ", optionNames)));
        });
  }

  @Override
  public Either<Error, ITxHash> finalizeSwap(ITxHash startTxHash,
                                             IChainAddress toAddress,
                                             IChainNumber amount) {
    return checkBalancesAndLimits(() ->
        startTxHash.getBytesEither().flatMap(startTxBytes -> {
          RemoteFunctionCall<TransactionReceipt> remoteFunctionCall;

          try {
            remoteFunctionCall = swapContract
                .finalizeSwap(startTxBytes,
                    toAddress.getAddress(),
                    amount.getValue());
          } catch (EnsResolutionException e) {
            e.printStackTrace();
            log.error("Error during call remove contract function ", e);
            return Either.left(new Error(Error.EC_ENS_RESOLUTION_EXCEPTION, Error.EM_ENS_RESOLUTION_EXCEPTION + ": " + e.getMessage()));
          }

          return tryEthCallCheckedFinal(() -> remoteFunctionCall.sendAsync().get())
              .flatMap(transactionReceipt -> {
                if (transactionReceipt.isStatusOK()) {
                  String hash = transactionReceipt.getTransactionHash();
                  if (hash == null)
                    return Either.left(new Error(Error.EC_HASH_NOT_FOUND, Error.EM_HASH_NOT_FOUND));

                  return Either.right(TxHashFactory.create(hash));
                }

                log.error("Error during send finalize swap tx : " + transactionReceipt.getStatus());
                return Either.left(new Error(3000, transactionReceipt.getStatus()));
              });

        }));
  }

  @Override
  public Either<Error, Optional<Transaction>> getTx(ITxHash hash) {
    return tryEthCallChecked(() -> {
      Optional<Transaction> receipt = web3j.ethGetTransactionByHash(hash.getHash()).sendAsync().get().getTransaction();
      if (receipt == null)
        return Either.left(new Error(Error.EC_RECEIPT_IS_NULL, Error.EM_RECEIPT_IS_NULL));
      return Either.right(receipt);
    });
  }

  @Override
  public Either<Error, Optional<TransactionReceipt>> getTxReceipt(ITxHash hash) {
    return tryEthCallChecked(() -> {
      Optional<TransactionReceipt> receipt = web3j.ethGetTransactionReceipt(hash.getHash()).sendAsync().get().getTransactionReceipt();
      if (receipt == null)
        return Either.left(new Error(Error.EC_RECEIPT_IS_NULL, Error.EM_RECEIPT_IS_NULL));
      return Either.right(receipt);
    });
  }

  @Override
  public Either<Error, List<EventInfo>> getEvents(IChainAddress address,
                                                  Long startBlockNum,
                                                  Long endBlockNum) {
    return getEvents(getLogs(address, startBlockNum, endBlockNum));
  }

  public Either<Error, List<EventInfo>> getEvents(Either<Error, List<EthLog.LogResult>> logResultsEither) {
    return logResultsEither.map(logResults ->
        logResults.stream()
            .map(logResult -> eventFactory.apply(logResult).map(t -> new EventInfo(chain, (Log) logResult.get(), t)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList()));
  }

  public Either<Error, List<EthLog.LogResult>> getLogs(IChainAddress address,
                                                       Long startBlockNum,
                                                       Long endBlockNum) {
    EthFilter filter = new EthFilter(
        new DefaultBlockParameterNumber(startBlockNum),
        new DefaultBlockParameterNumber(endBlockNum),
        address.getAddress());

    return tryEthCall(() -> {
      try {
        Request<?, EthLog> request = web3j.ethGetLogs(filter);
        return Either.right(request.send());
      } catch (IOException e) {
        e.printStackTrace();
        log.error("Error during getting logs ", e);
        return Either.left(new Error(Error.EC_GET_EVENTS_EXCEPTION, Error.EM_GET_EVENTS_EXCEPTION + " : " + e.getMessage()));
      }
    }).flatMap(ethLog -> {
      Response.Error error = ethLog.getError();
      if (error != null) {
        String errorMsg = error.getMessage();
        String errorDescr = "Error with code " + error.getCode() + " and error data " + error.getData() + " during getting logs: ";
        log.error(errorDescr, errorMsg);
        return Either.left(new Error(Error.EC_GET_LOGS_ERROR, Error.EM_GET_LOGS_ERROR + " : " + errorDescr + errorMsg));
      }

      List<EthLog.LogResult> logs = ethLog.getLogs();

      return Either.right(logs);
    });
  }

  @Override
  public void destroy() {
    web3j.shutdown();
  }

  @Override
  public Either<Error, IChainNumber> getFreeTokens() {
    return remoteFunctionCall(getERC20Contract(), erc20 -> {
      try {
        return Either.right(erc20.balanceOf(swapContractAddress.getAddress()));
      } catch (EnsResolutionException e) {
        e.printStackTrace();
        log.error("Error during call contract function ", e);
        return Either.left(new Error(Error.EC_ENS_RESOLUTION_EXCEPTION, Error.EM_ENS_RESOLUTION_EXCEPTION + ": " + e.getMessage()));
      }
    }, t -> ChainNumberFactory.create(t.toString()));
  }

  @Override
  public Either<Error, IChainNumber> getAllTokens() {
    return remoteFunctionCall(getERC20Contract(), erc20 -> {
      try {
        return Either.right(erc20.totalSupply());
      } catch (EnsResolutionException e) {
        e.printStackTrace();
        log.error("Error during call contract function ", e);
        return Either.left(new Error(Error.EC_ENS_RESOLUTION_EXCEPTION, Error.EM_ENS_RESOLUTION_EXCEPTION + ": " + e.getMessage()));
      }
    }, t -> ChainNumberFactory.create(t.toString()));
  }

  public static <R1, R2, R3> Either<Error, R3> remoteFunctionCall(Either<Error, R1> preF,
                                                                  Function<R1, Either<Error, RemoteFunctionCall<R2>>> midF,
                                                                  CheckedFunction<R2, R3> lastF) {
    return preF
        .flatMap(r1 -> midF.apply(r1))
        .flatMap(r2 -> tryEthCallCheckedFinal(() -> lastF.apply(r2.sendAsync().get())));
  }

  public static <R1, R2> Either<Error, R2> remoteFunctionCall(Supplier<Either<Error, RemoteFunctionCall<R1>>> midF,
                                                              CheckedFunction<R1, R2> lastF) {
    return midF.get()
        .flatMap(r2 -> tryEthCallCheckedFinal(() -> lastF.apply(r2.sendAsync().get())));
  }

  public static <R> Either<Error, R> tryEthCallCheckedFinal(CheckedSupplier<R> f) {
    try {
      return Either.right(f.call());
    } catch (InterruptedException e) {
      e.printStackTrace();
      log.error("Error during call contract function ", e);
      Error error = new Error(Error.EC_INTERRUPTED, Error.EM_INTERRUPTED + ": " + e.getMessage());
      return Either.left(error);
    } catch (ExecutionException e) {
      if (e.getMessage().contains("daily request count exceeded, request rate limited")) {
        log.warn("Daily requests limit exceeded");
        Error error = new Error(Error.EC_REQUESTS_LIMIT_EXCEEDED, Error.EM_REQUESTS_LIMIT_EXCEEDED + ": " + e.getMessage());
        return Either.left(error);
      } else if (e.getMessage().contains("java.net.SocketTimeoutException: timeout")) {
        log.error("Socket timeout exception ", e);
        Error error = new Error(Error.EC_SOCKET_TIMEOUT_EXCEPTION, Error.EM_SOCKET_TIMEOUT_EXCEPTION + ": " + e.getMessage());
        return Either.left(error);
      } else if (e.getMessage().contains("java.lang.RuntimeException: Error processing transaction request: nonce too low")) {
        log.error("Nonce too low error ", e);
        Error error = new Error(Error.EC_NONCE_TOOL_LOW, Error.EM_NONCE_TOOL_LOW + " : " + e.getMessage());
        return Either.left(error);
      }
      return Either.left(new Error(Error.EC_INTERRUPTED, Error.EM_INTERRUPTED + " : " + e.getMessage()));
    } catch (Throwable e) {
      log.error("Unknown exception ", e);
      e.printStackTrace();
      return Either.left(new Error(Error.EC_UNKNOWN_EXCEPTION, Error.EM_UNKNOWN_EXCEPTION + " : " + e.getMessage()));
    }
  }


  public static <R> Either<Error, R> tryEthCallChecked(CheckedSupplier<Either<Error, R>> f) {
    try {
      return f.call();
    } catch (InterruptedException e) {
      e.printStackTrace();
      log.error("Error during call contract function ", e);
      Error error = new Error(Error.EC_INTERRUPTED, Error.EM_INTERRUPTED);
      return Either.left(error);
    } catch (ExecutionException e) {
      if (e.getMessage().contains("daily request count exceeded, request rate limited")) {
        log.warn("Daily requests limit exceeded");
        Error error = new Error(Error.EC_REQUESTS_LIMIT_EXCEEDED, Error.EM_REQUESTS_LIMIT_EXCEEDED + ": " + e.getMessage());
        return Either.left(error);
      } else if (e.getMessage().contains("java.net.SocketTimeoutException: timeout")) {
        log.error("Socket timeout exception ", e);
        Error error = new Error(Error.EC_SOCKET_TIMEOUT_EXCEPTION, Error.EM_SOCKET_TIMEOUT_EXCEPTION + ": " + e.getMessage());
        return Either.left(error);
      }
      return Either.left(new Error(Error.EC_INTERRUPTED, Error.EM_INTERRUPTED));
    } catch (Throwable e) {
      log.error("Unknown exception ", e);
      e.printStackTrace();
      return Either.left(new Error(Error.EC_UNKNOWN_EXCEPTION, Error.EM_UNKNOWN_EXCEPTION));
    }
  }


  public static <R> Either<Error, R> tryEthCall(Supplier<Either<Error, R>> f) {
    try {
      return f.get();
    } catch (ClientConnectionException e) {
      e.printStackTrace();
      log.error("Error during call contract function ", e);
      if (e.getMessage().contains("daily request count exceeded, request rate limited"))
        return Either.left(new Error(Error.EC_REQUESTS_LIMIT_EXCEEDED, Error.EM_REQUESTS_LIMIT_EXCEEDED + ": " + e.getMessage()));
      else
        return Either.left(new Error(Error.EC_CLIENT_CONNECTION_EXCEPTION, Error.EM_CLIENT_CONNECTION_EXCEPTION + ": " + e.getMessage()));
    }
  }


}
