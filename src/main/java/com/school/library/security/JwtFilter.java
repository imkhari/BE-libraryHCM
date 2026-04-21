package com.school.library.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy thẻ từ header "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. Token chuẩn luôn bắt đầu bằng chữ "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Cắt bỏ chữ Bearer để lấy đoạn mã
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                System.out.println("Token không hợp lệ hoặc đã hết hạn");
            }
        }

        // 3. Nếu có Token hợp lệ và chưa được xác thực trong phiên này
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token)) {
                // Cấp quyền đi tiếp vào Controller
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. Cho phép đi tiếp
        filterChain.doFilter(request, response);
    }
}
