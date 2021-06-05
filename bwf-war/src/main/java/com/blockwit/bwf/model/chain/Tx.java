package com.blockwit.bwf.model.chain;

import com.blockwit.bwf.model.chain.converters.*;
import com.blockwit.bwf.model.chain.fields.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "txs")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Tx implements IChainNameProvider, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Chains chain;

  @Column(nullable = false)
  @Convert(converter = ChainAddressConverter.class)
  private IChainAddress fromAddr;

  @Column(nullable = false)
  @Convert(converter = ChainAddressConverter.class)
  private IChainAddress toAddr;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber amount;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber gas;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber gasPrice;

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
  private Long nonce;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TxStatus status;

  @Column(nullable = false)
  private Long createTime;

  @Column
  private Long updateTime;

}
