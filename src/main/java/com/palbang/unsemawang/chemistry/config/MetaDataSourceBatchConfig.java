package com.palbang.unsemawang.chemistry.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MetaDataSourceBatchConfig {
	@Primary // 충돌 방지를 위한 우선순위 설정
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource-meta")
	public DataSource metaDataSource() {

		return DataSourceBuilder.create().build();
	}

	@Primary // 충돌 방지를 위한 우선순위 설정
	@Bean
	public PlatformTransactionManager metaTransactionManager() {
		return new DataSourceTransactionManager(metaDataSource());
	}
}
