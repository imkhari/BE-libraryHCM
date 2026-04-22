package com.school.library.config;

import com.school.library.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // NHÓM API CÔNG KHAI (KHÔNG CẦN ĐĂNG NHẬP)
                        .requestMatchers("/api/v1/auth/login").permitAll() // Cổng đăng nhập
                        .requestMatchers(HttpMethod.POST, "/api/v1/analytics/visit").permitAll() // Đếm lượt view
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // Khách được đọc mọi thứ (bài viết, sách, thống kê...)

                        // NHÓM API QUẢN TRỊ (BẮT BUỘC PHẢI CÓ TOKEN HỢP LỆ)
                        .requestMatchers(HttpMethod.POST, "/api/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/articles/**").authenticated()

                        // Khóa toàn bộ các API lạ hoặc mới tạo sau này, muốn gọi phải có Token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}