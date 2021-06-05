package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.model.chain.SwapFinalizeTxLog;
import com.blockwit.bwf.model.chain.SwapStartTxLog;
import com.blockwit.bwf.model.chain.TxLogStatus;
import com.blockwit.bwf.model.chain.factories.BlockHashFactory;
import com.blockwit.bwf.model.chain.factories.BlockNumberFactory;
import com.blockwit.bwf.model.chain.factories.TxHashFactory;
import com.blockwit.bwf.model.chain.fields.ChainAddress;
import com.blockwit.bwf.model.chain.fields.ITxLog;
import com.blockwit.bwf.service.chains.common.events.EventInfo;
import com.blockwit.bwf.service.chains.common.events.SimpleEventSwapFinalized;
import com.blockwit.bwf.service.chains.common.events.SimpleEventSwapStarted;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapFinalized;
import com.blockwit.bwf.service.chains.common.events.sigs.EventSignatureSwapStarted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChainHelper {

	public static final String ethAddrRegexp = "^0x[a-fA-F0-9]{40}$";

	public static final String ethAmountRegexp = "^\\d{0,54}$";

	public static final String ethPrivateKeyRegexp = "^[a-fA-F0-9]{64}$";

	public static final String ethValueRegexp = "^(\\d{0,18}\\.\\d{0,18})|(\\d{0,18})$";

	public static final ChainAddress toETHAddr(String stringAddrCandidate) {
		if (stringAddrCandidate == null || stringAddrCandidate.isEmpty())
			return null;
		String stringAddrCandidatePerformed = stringAddrCandidate.toString();
		if (!stringAddrCandidate.matches(ethAddrRegexp))
			return null;
		return new ChainAddress(stringAddrCandidatePerformed);
	}

	public static final TxLogStatus getInitialStatus() {
		return TxLogStatus.WAIT_STORE_TX;
	}

	public SwapFinalizeTxLog convertToSwapFinalizeTxLog(EventInfo info) {
		Log log = info.getLog();
		SimpleEventSwapFinalized event = (SimpleEventSwapFinalized) info.getSimpleEvent();
		return SwapFinalizeTxLog.builder()
			.chain(info.getChain())
			.tokenAddr(event.getTokenAddr())
			.toAddr(event.getToAddr())
			.amount(event.getAmount())
			.createTxHash(event.getCreateTxHash())
			.txHash(TxHashFactory.create(log.getTransactionHash()))
			.blockHash(BlockHashFactory.create(log.getBlockHash()))
			.height(BlockNumberFactory.create(log.getBlockNumber().longValue()))
			.status(getInitialStatus())
			.createTime(System.currentTimeMillis())
			.build();
	}

	public SwapStartTxLog convertToSwapStartTxLog(EventInfo info) {
		Log log = info.getLog();
		SimpleEventSwapStarted event = (SimpleEventSwapStarted) info.getSimpleEvent();
		return SwapStartTxLog.builder()
			.chain(info.getChain())
			.tokenAddr(event.getTokenAddr())
			.fromAddr(event.getFromAddr())
			.amount(event.getAmount())
			.feeAmount(event.getFeeAmount())
			.txHash(TxHashFactory.create(log.getTransactionHash()))
			.blockHash(BlockHashFactory.create(log.getBlockHash()))
			.height(BlockNumberFactory.create(log.getBlockNumber().longValue()))
			.status(getInitialStatus())
			.createTime(System.currentTimeMillis())
			.build();
	}

	public List<ITxLog> convertToTxLogs(List<EventInfo> infos) {
		return infos.stream()
			.map(info -> {
				switch (info.getSimpleEvent().getEventName()) {
					case EventSignatureSwapStarted.NAME:
						return Optional.of(convertToSwapStartTxLog(info));
					case EventSignatureSwapFinalized.NAME:
						return Optional.of(convertToSwapFinalizeTxLog(info));
					default:
						log.warn("Unsupported event type " + info.getSimpleEvent().getEventName());
						return Optional.empty();
				}
			})
			.filter(Optional::isPresent)
			.map(t -> (ITxLog) t.get())
			.collect(Collectors.toList());
	}

}
