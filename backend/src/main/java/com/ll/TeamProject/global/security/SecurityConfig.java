package com.ll.TeamProject.global.security;

import com.ll.TeamProject.global.app.AppConfig;
import com.ll.TeamProject.global.rsData.RsData;
import com.ll.TeamProject.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    @Bean
    @Lazy
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // h2 콘솔 허용
                                .requestMatchers("/h2-console/**")
                                .permitAll()

                                // 관리자 로그인 로그아웃 인증 요청 허용
                                .requestMatchers("/admin/login", "/admin/logout", "/admin/verification-codes", "/admin/verification-codes/verify", "/admin/{username}/password")
                                .permitAll()

                                // 관리자 작업 권한 필요
                                .requestMatchers("/admin/**")
                                .hasAuthority("ROLE_ADMIN")

                                // 로그인 요청 허용
                                .requestMatchers("/login")
                                .permitAll()

                                // user 관련 인증 필요
                                .requestMatchers("/user/**")
                                .authenticated()

                                // 정적자원 허용
                                .requestMatchers("/static/**", "/images/**", "/css/**", "/js/**") // 정적 자원 예외 추가
                                .permitAll()

                                // 캘린더 API에 대한 인증 요구
                                .requestMatchers("/api/calendars/**")
                                .authenticated() // 캘린더 API에 대한 인증 요구

                                // 나머지 요청은 허용
                                // SpringDoc 관련 작업 하면서 임시로 (swagger 허용 필요)
                                .anyRequest()
                                .permitAll()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                )
                // 소셜 로그인
                .oauth2Login(
                        oauth2 -> {
                            // 소셜 로그인 성공시 실행
                            oauth2.successHandler(customOAuth2AuthenticationSuccessHandler);
                        }
                )
                // 시큐리티 필터에 커스텀 필터 추가
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(401);
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            new RsData("401-1", "사용자 인증정보가 올바르지 않습니다.")
                                                    )
                                            );
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(403);
                                            response.getWriter().write(
                                                    Ut.json.toString(
                                                            new RsData("403-1", "접근 권한이 없습니다.")
                                                    )
                                            );
                                        }
                                )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 오리진 설정
        configuration.setAllowedOriginPatterns(Arrays.asList(AppConfig.getSiteFrontUrl()));
        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        // 자격 증명 허용 설정
        configuration.setAllowCredentials(true);
        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // CORS 설정을 소스에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
