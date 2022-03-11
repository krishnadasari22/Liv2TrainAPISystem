package com.televisory.capitalmarket.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

	@Bean
	public ExecutorService createExecutorThread() {
		return Executors.newCachedThreadPool();
	}
	
}
