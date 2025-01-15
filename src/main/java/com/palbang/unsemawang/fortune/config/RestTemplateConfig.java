package com.palbang.unsemawang.fortune.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	// // HttpClient 구성
	// @Bean
	// public CloseableHttpClient httpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	// 	// 모든 인증서를 신뢰하도록 TrustStrategy 설정
	// 	TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
	//
	// 	// SSLContext 설정
	// 	SSLContext sslContext = SSLContexts.custom()
	// 		.loadTrustMaterial(null, acceptingTrustStrategy)
	// 		.build();
	//
	// 	// SSLConnectionSocketFactory 설정
	// 	SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
	// 		sslContext, NoopHostnameVerifier.INSTANCE);
	//
	// 	// 소켓 팩토리 레지스트리 구성
	// 	Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	// 		.register("https", sslSocketFactory)
	// 		.register("http", new PlainConnectionSocketFactory())
	// 		.build();
	//
	// 	// 연결 풀 관리
	// 	PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
	// 		socketFactoryRegistry);
	//
	// 	// HttpClient 생성
	// 	return HttpClients.custom()
	// 		.setConnectionManager(connectionManager)
	// 		.build();
	// }
	//
	// // HttpComponentsClientHttpRequestFactory 구성
	// @Bean
	// public HttpComponentsClientHttpRequestFactory factory(CloseableHttpClient httpClient) {
	// 	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
	// 	factory.setConnectTimeout(3000); // 연결 타임아웃 설정
	// 	return factory;
	// }
	//
	// // RestTemplate 구성
	// @Bean
	// public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
	// 	return new RestTemplate(factory);
	// }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}