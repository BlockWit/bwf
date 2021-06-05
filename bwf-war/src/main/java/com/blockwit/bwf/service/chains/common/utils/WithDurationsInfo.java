package com.blockwit.bwf.service.chains.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WithDurationsInfo {

	public static void process(String label, Runnable f) {
		long startTime = System.currentTimeMillis();
		log.trace("Starts " + label);
		f.run();
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.trace("Duration for \"" + label + "\" - " + duration);
	}

}
