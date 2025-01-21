package com.palbang.unsemawang.common.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

	private final ObjectMapper objectMapper;

	// 공통 포인트컷 정의
	@Pointcut("execution(* com.palbang.unsemawang..*.*(..)) "
		+ "&& !execution(* com.palbang.unsemawang.common..*.*(..)) "
		+ "&& !execution(* com.palbang.unsemawang.config..*.*(..)) "
		+ "&& !execution(* com.palbang.unsemawang.fortune.config..*.*(..))")
	public void applicationPackagePointcut() {
	}

	// 글자수 제한 로깅 메서드
	private String truncateLogData(Object data) {
		if (data == null) {
			return "null";
		}
		try {
			String jsonData = objectMapper.writeValueAsString(data);
			return jsonData.length() > 200 ? jsonData.substring(0, 200) + "..." : jsonData;
		} catch (JsonProcessingException e) {
			log.error("Error while processing JSON for logging: {}", e.getMessage());
			return "error_processing_data";
		}
	}

	// Around Advice: 실행 전후 한 번만 로깅
	@Around("applicationPackagePointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().toShortString();
		String arguments = truncateLogData(joinPoint.getArgs());

		// 메서드 실행 시작 로깅
		log.info("Executing method: {} with arguments: {}", methodName, arguments);

		try {
			// 메서드 실제 실행
			Object result = joinPoint.proceed();

			// 메서드 실행 완료 로깅
			log.info("Method executed: {} | Result: {}",
				methodName, truncateLogData(result));

			return result;
		} catch (Throwable t) {
			// 예외 발생 시 로깅
			log.error("Exception in method: {} with message: {}", methodName, t.getMessage(), t);
			throw t;
		}
	}
}
