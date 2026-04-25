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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // THÊM MỚI: CẤU HÌNH CHI TIẾT ĐỂ VƯỢT QUA LỖI CORS (OPTIONS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép mọi nguồn (domain) truy cập. Nếu muốn bảo mật hơn, thay "*" bằng URL của React (VD: "https://abc.vercel.app")
        configuration.setAllowedOriginPatterns(List.of("*"));
        // Bắt buộc phải có "OPTIONS" để Axios không bị chặn
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Cho phép đính kèm Token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // NHÓM API CÔNG KHAI (KHÔNG CẦN ĐĂNG NHẬP)
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll() // Cổng đăng nhập, đăng kí
                        .requestMatchers(HttpMethod.POST, "/api/v1/analytics/visit").permitAll() // Đếm lượt view
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // Khách được đọc mọi thứ

                        // NHÓM API QUẢN TRỊ (BẮT BUỘC PHẢI CÓ TOKEN HỢP LỆ)
                        .requestMatchers(HttpMethod.POST, "/api/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/articles/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/articles/**").authenticated()

                        .requestMatchers("/api/v1/tts/**").permitAll()

                        // Khóa toàn bộ các API lạ hoặc mới tạo sau này (bao gồm cả api quản lý users)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}