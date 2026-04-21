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
                .csrf(csrf -> csrf.disable()) // Tắt CSRF vì mình dùng Token
                .cors(cors -> cors.configure(http)) // Bật CORS cho React
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không dùng Session
                .authorizeHttpRequests(auth -> auth
                        // CÁC ĐƯỜNG DẪN CÔNG KHAI (Ai cũng vào được)
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/articles/**").permitAll() // Xem tin tức
                        .requestMatchers(HttpMethod.POST, "/api/v1/analytics/visit").permitAll() // Popup ghi nhận lượt xem

                        // CÁC ĐƯỜNG DẪN BẮT BUỘC CÓ TOKEN (Chỉ Admin)
                        .anyRequest().authenticated()
                )
                // Thêm ông bảo vệ JwtFilter vào đứng trước cổng
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
