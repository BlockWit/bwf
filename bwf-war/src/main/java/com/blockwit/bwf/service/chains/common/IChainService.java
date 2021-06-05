package com.blockwit.bwf.service.chains.common;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import com.blockwit.bwf.service.chains.common.events.EventInfo;
import io.vavr.control.Either;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.List;
import java.util.Optional;

public interface IChainService {

  Either<Error, IChainNumber> getInvokerBalance();

  Either<Error, Long> getHeight();

  Either<Error, List<EventInfo>> getEvents(IChainAddress address,
                                           Long startBlockNum,
                                           Long endBlockNum);

  Either<Error, Boolean> isFinalizedSwap(ITxHash startTxHash);

  Either<Error, ITxHash> finalizeSwap(ITxHash startTxHash,
                                      IChainAddress toAddress,
                                      IChainNumber amount);

  Either<Error, Optional<TransactionReceipt>> getTxReceipt(ITxHash hash);

  Either<Error, Optional<Transaction>> getTx(ITxHash hash);

  void destroy();

  Either<Error, IChainNumber> getFreeTokens();

  Either<Error, IChainNumber> getAllTokens();
}
