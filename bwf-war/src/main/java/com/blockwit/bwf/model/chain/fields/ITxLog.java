package com.blockwit.bwf.model.chain.fields;

import com.blockwit.bwf.model.chain.IChainNameProvider;
import com.blockwit.bwf.model.chain.TxLogStatus;

import java.io.Serializable;

public interface ITxLog extends IChainNameProvider, Serializable {

  Long getId();

  ITxHash getTxHash();

  IBlockHash getBlockHash();

  IBlockNumber getHeight();

  TxLogStatus getStatus();

  Long getCreateTime();

  Long getUpdateTime();

  ITxLog updateStatus(TxLogStatus txLogStatus);

  ITxLog updateStatusLog(TxLogStatus txLogStatus, String msg);

}
