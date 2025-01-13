package com.palbang.unsemawang.jwt;

import com.palbang.unsemawang.oauth2.dto.CustomOAuth2User;
import com.palbang.unsemawang.oauth2.dto.UserDTO;
import com.palbang.unsemawang.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


public class JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    //발급된 JWT토큰의 유효성을 검사하는 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        logger.debug("JWTFilter doFilterInternal 호출");
        Cookie[] cookies = request.getCookies();

        //해결방법?
        if(cookies == null){
            logger.debug("cookies null");
            filterChain.doFilter(request, response);
            return;
        }

        for(Cookie cookie : cookies){
            //System.out.println("쿠키이름:"+cookie.getName());
            if(cookie.getName().equals("Authorization")){
                authorization = cookie.getValue();
                break;
            }
        }

        //Authorization 헤더 검증
        if(authorization == null){
            logger.debug("token null");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 소멸 시간 검증
        String token = authorization;
        logger.debug("token:{}",token);
            if(jwtUtil.isExpired(token)){
            logger.debug("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료(필수)
            return;
        }
        //토큰에서 id email role 획득
        String id = jwtUtil.getId(token);
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        logger.debug("id:{}",id);
        logger.debug("email:{}",email);
        logger.debug("role:{}",role);

        //userEntity를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setEmail(email);
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //Authentication 객체 인증이 성공하면 SecurityContextHolder에 저장됨
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
