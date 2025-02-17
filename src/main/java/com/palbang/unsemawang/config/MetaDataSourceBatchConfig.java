package com.palbang.unsemawang.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MetaDataSourceBatchConfig {
	@BatchDataSource
	@Bean(name = "metaDataSource")
	@ConfigurationProperties(prefix = "spring.datasource-meta")
	public DataSource metaDataSource() {

		return DataSourceBuilder.create().build();
	}

	@BatchDataSource
	@Bean(name = "metaTransactionManager")
	public PlatformTransactionManager metaTransactionManager() {
		return new DataSourceTransactionManager(metaDataSource());
	}
}
