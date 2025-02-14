package com.palbang.unsemawang.chemistry.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
	basePackages = "com.palbang.unsemawang",
	entityManagerFactoryRef = "dataEntityManager", // 아래 사용한 메서드명
	transactionManagerRef = "dataTransactionManager" // 아래 사용한 메서드명
)
public class DomainDataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource-domain")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean // entity를 관리하는 Bean
	public LocalContainerEntityManagerFactoryBean dataEntityManager() {

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] {"com.palbang.unsemawang"});
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		HashMap<String, Object> properties = new HashMap<>();
		// 2개 DB를 연결하면 application.properties 설정 적용 X. 여기서 설정 해줘야 함
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.show_sql", "true");
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public PlatformTransactionManager dataTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(dataEntityManager().getObject());

		return transactionManager;
	}
}
