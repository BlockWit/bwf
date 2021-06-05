package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.repository.OptionRepository;
import com.blockwit.bwf.service.chains.common.utils.ChainHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OptionService {

  public final static String OPTION_APP_NAME = "APP_NAME";
  public final static String OPTION_APP_SHORT_NAME = "APP_SHORT_NAME";
  public final static String OPTION_APP_VERSION = "APP_VERSION";
  public final static String OPTION_APP_YEAR = "APP_YEAR";

  public static final String OPTION_LOGIN_ATTEMPTS_LIMIT = "OPTION_LOGIN_ATTEMPTS_LIMIT";
  public static final String OPTION_LOGIN_LOCK_PERIOD = "OPTION_LOGIN_LOCK_PERIOD";
  public static final String OPTION_LOGIN_TRY_PERIOD = "OPTION_LOGIN_TRY_PERIOD";

  public static final String OPTION_DEFIPULSE_URL = "OPTION_DEFIPULSE_URL";

  public final static String OPTION_Chain_CURRENT_BLOCK = "_CURRENT_BLOCK";
  public final static String OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ = "_LOGS_SCAN_RANGE_PER_SEQ";
  public final static String OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT = "_FORK_PREVENT_BLOCKS_COUNT";
  public final static String OPTION_Chain_PROVIDER = "_PROVIDER";
  public final static String OPTION_Chain_SWAPPER_ADDR = "_SWAPPER_ADDR";
  public final static String OPTION_Chain_SERVICE_POOL_MAX_TOTAL = "_SERVICE_POOL_MAX_TOTAL";
  public final static String OPTION_Chain_SERVICE_POOL_MAX_IDLE = "_SERVICE_POOL_MAX_IDLE";
  public final static String OPTION_Chain_SERVICE_POOL_MIN_IDLE = "_SERVICE_POOL_MIN_IDLE";
  public final static String OPTION_Chain_ADDRESS_LINK = "_CHAIN_ADDRESS_LINK";
  public final static String OPTION_Chain_TOKEN_LINK = "_CHAIN_TOKEN_LINK";
  public final static String OPTION_Chain_BLOCK_NUMBER_LINK = "_BLOCK_NUMBER_LINK";
  public final static String OPTION_Chain_TX_HASH_LINK = "_TX_HASH_LINK";
  public final static String OPTION_Chain_INVOKER_ADDRESS = "_INVOKER_ADDRESS";
  public final static String OPTION_Chain_INVOKER_PK = "_INVOKER_PK";
  public final static String OPTION_Chain_PASS_START_BLOCKS = "_PASS_START_BLOCKS";
  public final static String OPTION_Chain_START_BLOCK = "_START_BLOCK";

  public final static String OPTION_Chain_GAS_PRICE_PROVIDER = "_OPTION_Chain_GAS_PRICE_PROVIDER";
  public final static String OPTION_Chain_GAS_PRICE_LIMIT = "_OPTION_Chain_GAS_PRICE_LIMIT";
  public final static String OPTION_Chain_BALANCE_LIMIT = "_OPTION_Chain_BALANCE_LIMIT";

  public final static String OPTION_Chain_GAS_PRICE = "_GAS_PRICE";
  public final static String OPTION_Chain_GAS_LIMIT = "_GAS_LIMIT";

  public static final String PATTERN_ADDRESS = "[ADDR]";
  public static final String PATTERN_BLOCK_NUMBER = "[BLOCK_NUMBER]";
  public static final String PATTERN_TX_HASH = "[TX_HASH]";

  public final static String OPTION_PROFILE = "OPTION_PROFILE";

  public final static String OPTION_TYPE_STRING = "string";
  public final static String OPTION_TYPE_LONG = "long";
  public final static String OPTION_TYPE_INT = "int";
  public final static String OPTION_TYPE_ETH_ADDR = "eth_addr";
  public final static String OPTION_TYPE_ETH_AMOUNT = "eth_amount";

  public final static String OPTION_PROFILE_VALUE_MAIN = "OP_MAIN";


  private final OptionRepository optionRepository;
  private final Map<String, Option> defaultOptions;
  private final List<String> defaultOptionNames;

  public static final String SEPARATOR = "_";

  public OptionService(OptionRepository optionRepository) {
    this.optionRepository = optionRepository;

    defaultOptions = new HashMap<>();

    defaultOptions.putAll(Map.of(
        OPTION_APP_NAME, new Option(0L, OPTION_APP_NAME, OPTION_TYPE_STRING, "TenSet", "Application full name"),
        OPTION_APP_VERSION, new Option(0L, OPTION_APP_VERSION, OPTION_TYPE_STRING, "next", "Application version"),
        OPTION_APP_SHORT_NAME, new Option(0L, OPTION_APP_SHORT_NAME, OPTION_TYPE_STRING, "TenSet", "Application short name"),
        OPTION_APP_YEAR, new Option(0L, OPTION_APP_YEAR, OPTION_TYPE_STRING, "2021", "Application year"),
        OPTION_LOGIN_ATTEMPTS_LIMIT, new Option(0L, OPTION_LOGIN_ATTEMPTS_LIMIT, OPTION_TYPE_INT, "3", "Count of bad login attempts before account locking"),
        OPTION_LOGIN_LOCK_PERIOD, new Option(0L, OPTION_LOGIN_LOCK_PERIOD, OPTION_TYPE_LONG, "86400000", "Account lock period in case of login attempts limit exceeded"),
        OPTION_LOGIN_TRY_PERIOD, new Option(0L, OPTION_LOGIN_TRY_PERIOD, OPTION_TYPE_LONG, "3600000", "Period between two bad login attempts to increase bad attempts counter")
    ));

    // Common chain options
    defaultOptions.putAll(Map.of(
        OPTION_PROFILE, new Option(0L, OPTION_PROFILE, OPTION_TYPE_STRING, OPTION_PROFILE_VALUE_MAIN, "Profile prefix for chain properties")
    ));

    // ETH chain options
    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT, OPTION_TYPE_LONG, "12", "ETH fork protection blocks count"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_CURRENT_BLOCK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_CURRENT_BLOCK, OPTION_TYPE_LONG, "10278104", "ETH current processing block"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_START_BLOCK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_START_BLOCK, OPTION_TYPE_LONG, "10278104", "ETH start scanner block"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_PROVIDER, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_PROVIDER, OPTION_TYPE_STRING, "https://ropsten.infura.io/v3/323b78f3ec52434289ade205e2543f20", "ETH provider"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ, OPTION_TYPE_LONG, "1000", "Logs range per scan iteration"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SWAPPER_ADDR, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SWAPPER_ADDR, OPTION_TYPE_ETH_ADDR, "0xd403d14e1ad86a287652fd28594a967d5e9b9b7a", "Swapper contract ethereum address"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_PASS_START_BLOCKS, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_PASS_START_BLOCKS, OPTION_TYPE_LONG, "20", "Pass start blocks")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MAX_TOTAL, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MAX_TOTAL, OPTION_TYPE_INT, "10", "Max total ETH Chain Service instances in pool"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MAX_IDLE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MAX_IDLE, OPTION_TYPE_INT, "9", "Max idle ETH Chain Service instances in pool"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MIN_IDLE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_SERVICE_POOL_MIN_IDLE, OPTION_TYPE_INT, "1", "Min total ETH Chain Service instances in pool")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_TOKEN_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_TOKEN_LINK, OPTION_TYPE_STRING, "https://ropsten.etherscan.io/token/" + PATTERN_ADDRESS, "Explorer token link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_ADDRESS_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_ADDRESS_LINK, OPTION_TYPE_STRING, "https://ropsten.etherscan.io/address/" + PATTERN_ADDRESS, "Explorer address link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_BLOCK_NUMBER_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_BLOCK_NUMBER_LINK, OPTION_TYPE_STRING, "https://ropsten.etherscan.io/block/" + PATTERN_BLOCK_NUMBER, "Explorer block link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_TX_HASH_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_TX_HASH_LINK, OPTION_TYPE_STRING, "https://ropsten.etherscan.io/tx/" + PATTERN_TX_HASH, "Explorer transaction hash link pattern")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE, OPTION_TYPE_ETH_AMOUNT, "20000000000", "Gas price"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_LIMIT, OPTION_TYPE_ETH_AMOUNT, "200000", "Gas limit for finalize swap transaction")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_INVOKER_ADDRESS, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_INVOKER_ADDRESS, OPTION_TYPE_ETH_ADDR, "0xcA4744261da0561bd9A8B7f8D913A12c2ac72972", "Invoker address"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_INVOKER_PK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_INVOKER_PK, OPTION_TYPE_STRING, "1e27f610b7f7c8dcb5b6775b2305c59571ed34164f4200616bb4ed0a8835706f", "Invoker private key")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE_PROVIDER, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE_PROVIDER, OPTION_TYPE_STRING, "StaticOptionsBasedGasProvider", "Gas price provider"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_GAS_PRICE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "50000000000", "Gas price limit to stop tasks until it down"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_BALANCE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.Ethereum + OPTION_Chain_BALANCE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "500000000000000000", "Balance limit to stop tasks until it filled")
    ));


    // BSC chain options

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_PROVIDER, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_PROVIDER, OPTION_TYPE_STRING, "StaticOptionsBasedGasProvider", "Gas price provider"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "50000000000", "Gas price limit to stop tasks until it down"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BALANCE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BALANCE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "500000000000000000", "Balance limit to stop tasks until it filled")
    ));


    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT, OPTION_TYPE_LONG, "12", "BSC fork protection blocks count"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_CURRENT_BLOCK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_CURRENT_BLOCK, OPTION_TYPE_LONG, "9038553", "BSC current processing block"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_START_BLOCK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_START_BLOCK, OPTION_TYPE_LONG, "9038553", "BSC start scanner block"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_PROVIDER, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_PROVIDER, OPTION_TYPE_STRING, "https://data-seed-prebsc-1-s1.binance.org:8545", "BSC provider"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_LOGS_SCAN_RANGE_PER_SEQ, OPTION_TYPE_LONG, "1000", "Logs range per scan iteration"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SWAPPER_ADDR, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SWAPPER_ADDR, OPTION_TYPE_ETH_ADDR, "0xa67aba13924ee886c007b61175f9a952c741c17b", "Swapper contract BSC address"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_PASS_START_BLOCKS, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_PASS_START_BLOCKS, OPTION_TYPE_LONG, "20", "Pass start blocks")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MAX_TOTAL, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MAX_TOTAL, OPTION_TYPE_INT, "10", "Max total Chain Service instances in pool"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MAX_IDLE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MAX_IDLE, OPTION_TYPE_INT, "9", "Max idle BSC Chain Service instances in pool"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MIN_IDLE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_SERVICE_POOL_MIN_IDLE, OPTION_TYPE_INT, "1", "Min total BSC Chain Service instances in pool")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_TOKEN_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_TOKEN_LINK, OPTION_TYPE_STRING, "https://testnet.bscscan.com/token/" + PATTERN_ADDRESS, "Explorer token link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_ADDRESS_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_ADDRESS_LINK, OPTION_TYPE_STRING, "https://testnet.bscscan.com/address/" + PATTERN_ADDRESS, "Explorer address link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BLOCK_NUMBER_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BLOCK_NUMBER_LINK, OPTION_TYPE_STRING, "https://testnet.bscscan.com/block/" + PATTERN_BLOCK_NUMBER, "Explorer block link pattern"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_TX_HASH_LINK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_TX_HASH_LINK, OPTION_TYPE_STRING, "https://testnet.bscscan.com/tx/" + PATTERN_TX_HASH, "Explorer transaction hash link pattern")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE, OPTION_TYPE_ETH_AMOUNT, "20000000000", "Gas price"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_LIMIT, OPTION_TYPE_ETH_AMOUNT, "200000", "Gas limit for finalize swap transaction")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_INVOKER_ADDRESS, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_INVOKER_ADDRESS, OPTION_TYPE_ETH_ADDR, "0xF58814eA9e2968c6835Cc86AA6dCa843DfDB85df", "Invoker address"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_INVOKER_PK, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_INVOKER_PK, OPTION_TYPE_STRING, "91eb6db43ad3c61ba29778b39af65bf2e6860826cfe1ab435eeaa0a83dba6f21", "Invoker private key")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_PROVIDER, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_PROVIDER, OPTION_TYPE_STRING, "StaticOptionsBasedGasProvider", "Gas price provider"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_GAS_PRICE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "50000000000", "Gas price limit to stop tasks until it down"),
        OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BALANCE_LIMIT, new Option(0L, OPTION_PROFILE_VALUE_MAIN + SEPARATOR + Chains.BinanceSmartChain + OPTION_Chain_BALANCE_LIMIT, OPTION_TYPE_ETH_AMOUNT, "5000000000000000000", "Balance limit to stop tasks until it filled")
    ));


    defaultOptions.putAll(Map.of(
        OPTION_DEFIPULSE_URL, new Option(0L, OPTION_DEFIPULSE_URL, OPTION_TYPE_STRING, "https://data-api.defipulse.com/api/v1/egs/api/ethgasAPI.json?api-key=5a7c274959812caaac463dd527faa00c79101a3729cf4bd7c0f78fc50597", "Defipulse API URL for Ethereum Gas Price station")
    ));


    defaultOptionNames = defaultOptions.keySet().stream().collect(Collectors.toList());

    getAllDefaultValues();
  }

  @Transactional
  public Map<String, String> getAllDefaultValues() {
    Map<String, String> namesToValues = optionRepository.findByNameIn(defaultOptionNames)
        .stream().collect(Collectors.toMap(Option::getName, Option::getValue));

    if (!namesToValues.keySet().containsAll(defaultOptionNames)) {
      for (String name : defaultOptionNames) {
        if (!namesToValues.containsKey(name)) {
          optionRepository.save(defaultOptions.get(name));
        }
      }

      namesToValues = optionRepository.findByNameIn(defaultOptionNames)
          .stream().collect(Collectors.toMap(Option::getName, Option::getValue));
    }

    return namesToValues;
  }

  @Transactional
  public Optional<Option> update(Option option) {
    return _update(option);
  }

  public Optional<Option> _update(Option option) {
    return optionRepository.findById(option.getId()).flatMap(t -> {
      Option newOption = optionRepository.save(option);
      return Optional.of(newOption);
    });
  }

  private String _getActiveProfile() {
    return findByName(OPTION_PROFILE).get().getValue();
  }

  public String getActiveProfile() {
    return _getActiveProfile();
  }

  public Optional<Option> findProfiledByName(String name) {
    String profile = _getActiveProfile();
    return optionRepository.findByName(profile + SEPARATOR + name);
  }

  public Set<Option> findProfiledByNameIn(List<String> optionNames) {
    String profile = _getActiveProfile();
    return optionRepository.findByNameIn(optionNames.stream()
        .map(t -> profile + "_" + t).collect(Collectors.toList()));
  }

  public Optional<Option> findByName(String name) {
    return optionRepository.findByName(name);
  }

  public Set<Option> findByNameIn(List<String> optionNames) {
    return optionRepository.findByNameIn(optionNames);
  }

  public static Object getPerformedValue(Option curOption) {
    Object result = null;
    if (curOption.getType().equals(OptionService.OPTION_TYPE_ETH_AMOUNT)) {
      result = new BigInteger(curOption.getValue());
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_LONG)) {
      try {
        result = Long.parseLong(curOption.getValue());
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_INT)) {
      try {
        result = Integer.parseInt(curOption.getValue());
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_ETH_ADDR)) {
      result = ChainHelper.toETHAddr(curOption.getValue());
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_STRING)) {
      result = curOption.getValue();
    }

    if (result == null)
      log.error("Option " + curOption.getName() + " with type - " + curOption.getType() + " have wrong value: \"" + curOption.getValue() + "\"");

    return result;
  }

  @Transactional
  public Optional<Option> updateProfiled(Option option) {
    String profile = _getActiveProfile();
    return optionRepository.findById(option.getId()).flatMap(t -> {
      Option newOption = optionRepository.save(option.toBuilder().name(profile + SEPARATOR + option.getName()).build());
      return Optional.of(newOption.toBuilder().name(option.getName().substring(profile.length() + SEPARATOR.length())).build());
    });
  }
}
