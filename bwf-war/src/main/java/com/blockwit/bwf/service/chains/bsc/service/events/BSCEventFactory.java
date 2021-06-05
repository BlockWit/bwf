package com.blockwit.bwf.service.chains.bsc.service.events;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.service.chains.common.events.EventFactory;
import com.blockwit.bwf.service.chains.common.events.SimpleEventSwapFinalized;
import com.blockwit.bwf.service.chains.common.events.SimpleEventSwapStarted;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapFinalized;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapStarted;
import org.springframework.stereotype.Component;

@Component
public class BSCEventFactory extends EventFactory {

	public BSCEventFactory() {
		EventSignatureSwapStarted signObj1 = new EventSignatureSwapStarted();
		eventSignatureMap.put(signObj1.getSign(), log -> new SimpleEventSwapStarted(Chains.BinanceSmartChain, signObj1, log));

		EventSignatureSwapFinalized signObj2 = new EventSignatureSwapFinalized();
		eventSignatureMap.put(signObj2.getSign(), log -> new SimpleEventSwapFinalized(Chains.BinanceSmartChain, signObj2, log));
	}

}
