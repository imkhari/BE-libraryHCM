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
                        // 1. Mở cửa cho API Đăng nhập
                        .requestMatchers("/api/v1/auth/login").permitAll()

                        // 2. Mở cửa cho API đếm View
                        .requestMatchers(HttpMethod.POST, "/api/v1/analytics/visit").permitAll()

                        // 3. Cho phép Khách vãng lai gọi TẤT CẢ các API có phương thức GET (Đọc dữ liệu)
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()

                        // 4. Các API còn lại (Đăng bài, Xóa bài...) vẫn bị khóa chặt, bắt buộc phải có thẻ JWT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
