package com.school.library.controller;

import com.school.library.entity.User;
import com.school.library.security.JwtUtil;
import com.school.library.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.authenticate(request.getUsername(), request.getPassword());
            // Tạo Token
            String token = jwtUtil.generateToken(user.getUsername());

            // Trả về thông tin user khi đăng nhập thành công
            return ResponseEntity.ok(new LoginResponse(token, user.getFullName(), user.getRole()));
        } catch (RuntimeException e) {
            // Trả về mã lỗi 401 (Unauthorized) nếu sai thông tin
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Class DTO (Data Transfer Object) dùng để hứng dữ liệu JSON từ Frontend gửi lên
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    // Class hứng dữ liệu trả về
    @Data
    public static class LoginResponse {
        private final String token;
        private final String fullName;
        private final String role;
    }
}
