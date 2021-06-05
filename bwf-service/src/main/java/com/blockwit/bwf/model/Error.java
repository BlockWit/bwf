package com.blockwit.bwf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

  public static final int EC_HASH_NOT_FOUND = 0;

  public static final int EC_GETTING_RECEIPT_EXCEPTION = 1;

  public static final int EC_RECEIPT_IS_NULL = 2;

  public static final int EC_FAILED_GETTING_NONCE = 3;

  public static final int EC_GET_EVENTS_EXCEPTION = 4;

  public static final int EC_GET_LOGS_ERROR = 5;

  public static final int EC_INTERRUPTED = 6;

  public static final int EC_EXECUTED_EXCEPTION = 7;

  public static final int EC_BYTES_DECODE_EXCEPTION = 8;

  public static final int EC_ENS_RESOLUTION_EXCEPTION = 9;

  public static final int EC_GETTING_TX_EXCEPTION = 10;

  public static final int EC_CAN_NOT_UPDATE_TASK = 11;

  public static final int EC_WRONG_TASK_STATUS = 12;

  public static final int EC_TASK_NOT_FOUND = 13;

  public static final int EC_LOGIN_EXISTS = 14;

  public static final int EC_EMAIL_EXISTS = 15;

  public static final int EC_CAN_NOT_CREATE_ACCOUNT = 16;

  public static final int EC_CAN_NOT_UPDATE_ACCOUNT = 17;

  public static final int EC_CAN_NOT_UPDATE_ACCOUNT_PASSWORD = 18;

  public static final int EC_ACCOUNT_NO_FOUND = 19;

  public static final int EC_CAN_NOT_GET_OPTIONS = 20;

  public static final int EC_GAS_PRICES_LIMIT_EXCEEDED = 21;

  public static final int EC_CAN_NOT_GET_BALANCE = 22;

  public static final int EC_BALANCE_LIMIT_EXCEEDED = 23;

  public static final int EC_CLIENT_CONNECTION_EXCEPTION = 24;

  public static final int EC_REQUESTS_LIMIT_EXCEEDED = 25;

  public static final int EC_UNKNOWN_EXCEPTION = 26;

  public static final int EC_GET_BLOCK_NUMBER_ERROR = 27;

  public static final int EC_SOCKET_TIMEOUT_EXCEPTION = 28;

  public static final int EC_CAN_NOT_UPDATE_SWAP = 29;

  public static final int EC_WRONG_SWAP_STATUS = 30;

  public static final int EC_SWAP_NOT_FOUND = 31;

  public static final int EC_NONCE_TOOL_LOW = 32;

  public static final String EM_HASH_NOT_FOUND = "Hash not found";

  public static final String EM_GETTING_RECEIPT_EXCEPTION = "Can't get receipt";

  public static final String EM_RECEIPT_IS_NULL = "Receipt is null";

  public static final String EM_FAILED_GETTING_NONCE = "Can't get nonce";

  public static final String EM_GET_EVENTS_EXCEPTION = "Get events exception";

  public static final String EM_GET_LOGS_ERROR = "Get logs error";

  public static final String EM_INTERRUPTED = "Interrupted ";

  public static final String EM_EXECUTED_EXCEPTION = "Executed exception";

  public static final String EM_BYTES_DECODE_EXCEPTION = "Exception during bytes decode";

  public static final String EM_ENS_RESOLUTION_EXCEPTION = "ENS resolution exception";

  public static final String EM_GETTING_TX_EXCEPTION = "Can't get transaction for ";

  public static final String EM_LOGIN_EXISTS = "Login already exists ";

  public static final String EM_EMAIL_EXISTS = "Email already exists ";

  public static final String EM_CAN_NOT_CREATE_ACCOUNT = "Can't create account ";

  public static final String EM_CAN_NOT_UPDATE_ACCOUNT = "Can not update account ";

  public static final String EM_CAN_NOT_UPDATE_ACCOUNT_PASSWORD = "Can not update account password ";

  public static final String EM_ACCOUNT_NO_FOUND = "Account not found ";

  public static final String EM_CAN_NOT_GET_OPTIONS = "Can't get options  ";

  public static final String EM_GAS_PRICES_LIMIT_EXCEEDED = "Gas price limit exceeded ";

  public static final String EM_CAN_NOT_GET_BALANCE = "Can't get balance ";

  public static final String EM_BALANCE_LIMIT_EXCEEDED = "Balance limit exceeded ";

  public static final String EM_CLIENT_CONNECTION_EXCEPTION = "Client connection exception ";

  public static final String EM_REQUESTS_LIMIT_EXCEEDED = "Requests limit exceeded ";

  public static final String EM_UNKNOWN_EXCEPTION = "Unknown exception ";

  public static final String EM_GET_BLOCK_NUMBER_ERROR = "Get block number error ";

  public static final String EM_SOCKET_TIMEOUT_EXCEPTION = "Socket timeout exception ";

  public static final String EM_CAN_NOT_UPDATE_SWAP = "Can not update swap ";

  public static final String EM_WRONG_SWAP_STATUS = "Wrong swap status ";

  public static final String EM_SWAP_NOT_FOUND = "Swap not found ";

  public static final String EM_NONCE_TOOL_LOW = "Nonce too low ";

  private int code;

  private String descr;

}
