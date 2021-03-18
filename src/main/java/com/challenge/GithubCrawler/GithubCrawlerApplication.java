package com.challenge.GithubCrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableCaching(proxyTargetClass = true)
public class GithubCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubCrawlerApplication.class, args);
	}

	/**
	 * Async functions threads configuration.
	 *
	 * @return Executor of the threads.
	 */
	@Bean(name = "asyncExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1000);
		executor.setMaxPoolSize(1000);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix("GithubLookup-");
		executor.initialize();
		return executor;
	}

}
