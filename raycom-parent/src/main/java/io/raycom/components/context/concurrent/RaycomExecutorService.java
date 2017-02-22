package io.raycom.components.context.concurrent;

import io.raycom.common.config.Constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

@Component
public class RaycomExecutorService {
	
	private int cpuNums = Runtime.getRuntime().availableProcessors();
	
	private ExecutorService mailService = Executors.newFixedThreadPool(cpuNums * Constant.POOL_SIZE);
	
	private ExecutorService baseService = Executors.newFixedThreadPool(cpuNums * Constant.POOL_SIZE);

	public ExecutorService getMailService() {
		return mailService;
	}

	public ExecutorService getBaseService() {
		return baseService;
	}
	
	
	
	

}
