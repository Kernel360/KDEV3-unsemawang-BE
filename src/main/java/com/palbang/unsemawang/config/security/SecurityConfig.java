package com.palbang.unsemawang.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //기본 Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //oauth2
        //http
                //.oauth2Login(Customizer.withDefaults());

        //경로별 인가 작업
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/").permitAll()
//                        .anyRequest().authenticated());

        //세션 설정 : STATELESS
//        http
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll());
        return http.build();
    }
    /**
     * 패스워드 암호화를 위한 BCryptPasswordEncoder Bean 등록
     * @return PasswordEncoder 객체
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
