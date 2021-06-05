package com.blockwit.bwf.model.chain;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.chain.converters.ChainAddressConverter;
import com.blockwit.bwf.model.chain.converters.ChainNumberConverter;
import com.blockwit.bwf.model.chain.converters.TxHashConverter;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import com.blockwit.bwf.model.chain.fields.IChainNumber;
import com.blockwit.bwf.model.chain.fields.ITxHash;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "swaps")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Swap implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private SwapStatus status;

  @Column(nullable = false)
  @Convert(converter = ChainAddressConverter.class)
  private IChainAddress address;

  @Column(nullable = false)
  @Convert(converter = ChainNumberConverter.class)
  private IChainNumber amount;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private SwapDirection direction;

  @Column
  @Convert(converter = TxHashConverter.class)
  private ITxHash startTxHash;

  @Column
  @Convert(converter = TxHashConverter.class)
  private ITxHash finalizeTxHash;

  @Column
  private String log;

  @Column
  private String prevLog;

  @Column(nullable = false)
  private Long createTime;

  @Column
  private Long updateTime;

  public Swap updateError(SwapStatus status, String errDescr) {
    return toBuilder()
        .status(status)
        .prevLog(this.log)
        .log(errDescr)
        .updateTime(System.currentTimeMillis())
        .build();
  }

  public Swap updateError(SwapStatus status, Error error) {
    return updateError(status, error.getDescr());
  }

  public Swap update(SwapStatus status) {
    return toBuilder()
        .status(status)
        .prevLog(this.log)
        .log(null)
        .updateTime(System.currentTimeMillis())
        .build();
  }

  public Swap updateFinalized(SwapStatus status, ITxHash txHash) {
    return toBuilder()
        .status(status)
        .finalizeTxHash(txHash)
        .prevLog(this.log)
        .log(null)
        .updateTime(System.currentTimeMillis())
        .build();
  }

}
