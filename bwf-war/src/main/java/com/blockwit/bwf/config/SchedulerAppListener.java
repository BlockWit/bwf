package com.blockwit.bwf.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

//@Component
public class SchedulerAppListener /*implements ApplicationListener<ContextClosedEvent>*/ {
/*
	private ThreadPoolTaskExecutor executor;
	private ThreadPoolTaskScheduler scheduler;

	public SchedulerAppListener(ThreadPoolTaskExecutor executor, ThreadPoolTaskScheduler scheduler) {
		this.executor = executor;
		this.scheduler = scheduler;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		scheduler.shutdown();
		executor.shutdown();
	}*/
}
