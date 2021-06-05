package com.blockwit.bwf.service.chains.common.service;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
public class ERC20Contract extends Contract {

  public static final String CONTRACT_BIN = "";

  public static final String FUNC_BALANCE_OF = "balanceOf";

  public static final String FUNC_TOTAL_SUPPLY = "totalSupply";

  protected ERC20Contract(Web3j web3j,
                          String contractAddress,
                          TransactionManager transactionManager,
                          ContractGasProvider gasProvider) {
    super(CONTRACT_BIN, contractAddress, web3j, transactionManager, gasProvider);
  }

  public RemoteFunctionCall<BigInteger> balanceOf(String param0) {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCE_OF,
        Arrays.asList(new org.web3j.abi.datatypes.Address(param0)),
        Arrays.asList(new TypeReference<Uint256>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<BigInteger> totalSupply() {
    final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTAL_SUPPLY,
        Arrays.asList(),
        Arrays.asList(new TypeReference<Uint256>() {
        }));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

}
