package org.shopping.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.shopping.models.member.LoginFailureHandler;
import org.shopping.models.member.LoginSuccessHandler;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /** 회원 인증 */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("userId")
                    .passwordParameter("userPw")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });

        http.logout(f -> {
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login");
        });

        /** 회원 인가 */
        http.authorizeHttpRequests(f -> {
            f
                    /* 회원 전용 */
                    .requestMatchers("/mypage/**").authenticated()
                    /* 관리자 전용 */
                    .requestMatchers("/admin/**").hasAuthority("ADMIN") // 관리자만 접근 가능
                    /* 회원 / 비회원 / 관리자 접근 가능 */
                    .anyRequest().permitAll();
        });

        /** 페이지 접근 권한 없는 경우 상세 설정 */
        http.exceptionHandling(f -> {
            f.authenticationEntryPoint((req, resp, e) -> {
                String URI = req.getRequestURI();
                /* mypage -> login */
                if (URI.indexOf("/mypage") != -1){
                    resp.sendRedirect(req.getContextPath() + "/member/login");
                    /* admin -> 401error */
                } else if (URI.indexOf("/admin") != -1) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT AUTHORZIED");
                }
            });
        });
        return http.build();
    }

    /** Security 설정 배제 */
    public WebSecurityCustomizer webSecurityCustomizer(){
        return w -> w.ignoring().requestMatchers("/static/css/**", "/js/**", "/images/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}