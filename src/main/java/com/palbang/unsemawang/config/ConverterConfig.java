package com.palbang.unsemawang.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ConverterConfig {

	public ConverterConfig(MappingJackson2HttpMessageConverter converter) {
		// octet-stream 타입을 허용하도록 설정
		List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}
}
