package com.example.demo.config;

import com.example.demo.Component.JwtAuthenticationFilter;
import com.example.demo.Component.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
public class WebSecurityConfig  {
    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        http
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF 保护
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 使用无状态会话
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll() // 允许访问H2控制台
                        .requestMatchers("/api/auth/**").permitAll()  // 允许未认证访问的端点
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()  // 其他端点需要认证
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// 在 UsernamePasswordAuthenticationFilter 之前添加 JWT 过滤器

        return  http.build();
    }
}
