package com.palbang.unsemawang.common.config.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;

	public BatchConfig(DataSource dataSource, PlatformTransactionManager transactionManager) {
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}

	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setTransactionManager(transactionManager);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	/**
	 * Spring Batch 트랜잭션 관리
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return transactionManager;
	}
}
