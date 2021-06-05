package com.blockwit.bwf.service.chains.common.events.sigs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChainEventFieldParam {

	private String name;

	private ChainTypes type;

	private boolean indexed;

}
