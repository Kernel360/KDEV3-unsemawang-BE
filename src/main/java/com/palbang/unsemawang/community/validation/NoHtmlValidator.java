package com.palbang.unsemawang.community.validation;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * HTML 태그 필터링을 위한 커스텀 검증기
 * <h3>, <p> 태그만 허용하고, 나머지 모든 html 태그를 차단
 */
public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {
	private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<(?!/?(h3|p)\\b)[^>]+>");

	/**
	 *
	 * @param value 검증할 문자열
	 * @param context
	 * @return 유효하면 true, html 태그 포함 시 false
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null 값은 검증하지 않음
		}

		// html 엔티티를 일반 텍스트로 변환
		String unescaped = StringEscapeUtils.unescapeHtml4(value);

		// 허용되지 않은 html 태그가 포함되어 있으면 false 반환
		return !HTML_TAG_PATTERN.matcher(unescaped).find();
	}
}