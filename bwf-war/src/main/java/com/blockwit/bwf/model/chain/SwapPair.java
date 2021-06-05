package com.blockwit.bwf.model.chain;

import com.blockwit.bwf.model.chain.converters.ChainAddressConverter;
import com.blockwit.bwf.model.chain.fields.IChainAddress;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "swap_pairs")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SwapPair implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private SwapPairStatus status;

	@Column(nullable = false)
	@Convert(converter = ChainAddressConverter.class)
	private IChainAddress bep20Addr;

	@Column(nullable = false)
	@Convert(converter = ChainAddressConverter.class)
	private IChainAddress eth20Addr;

	@Column(nullable = false, unique = true)
	private String symbol;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer decimals;

	@Column(nullable = false)
	private Long createTime;

}
