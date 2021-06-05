package com.blockwit.bwf.model.chain;

import com.blockwit.bwf.model.chain.converters.*;
import com.blockwit.bwf.model.chain.fields.*;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "swap_start_tx_logs")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SwapStartTxLog implements ITxLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Chains chain;

  @Column(nullable = false)
  @Convert(converter = ChainAddressConverter.class)
  private IChainAddress tokenAddr;

  @Column(nullable = false)
  @Convert(converter = ChainAddressConverter.class)
  private IChainAddress fromAddr;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber amount;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber feeAmount;

  @Column(nullable = false)
  @Convert(converter = TxHashConverter.class)
  private ITxHash txHash;

  @Column(nullable = false)
  @Convert(converter = BlockHashConverter.class)
  private IBlockHash blockHash;

  @Column(nullable = false)
  @Convert(converter = BlockNumberConverter.class)
  private IBlockNumber height;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TxLogStatus status;

  @Column
  private String log;

  @Column(nullable = false)
  private Long createTime;

  @Column
  private Long updateTime;

  @Override
  public SwapStartTxLog updateStatus(TxLogStatus txLogStatus) {
    return toBuilder()
        .status(txLogStatus)
        .log(null)
        .updateTime(System.currentTimeMillis())
        .build();
  }

  @Override
  public SwapStartTxLog updateStatusLog(TxLogStatus txLogStatus, String msg) {
    return toBuilder()
        .status(txLogStatus)
        .log(msg)
        .updateTime(System.currentTimeMillis())
        .build();
  }

}
