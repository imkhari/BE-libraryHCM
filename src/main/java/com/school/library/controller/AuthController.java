package com.school.library.controller;

import com.school.library.entity.User;
import com.school.library.repository.UserRepository;
import com.school.library.security.JwtUtil;
import com.school.library.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("POSTMAN ĐANG GỬI LÊN: Username = [" + request.getUsername() + "], Password = [" + request.getPassword() + "]");
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Bước 1: Kiểm tra mã bảo mật nội bộ của trường
            String INTERNAL_CODE = "THAIPHIEN";
            if (!INTERNAL_CODE.equals(request.getSecretCode())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Mã bảo mật nội bộ không chính xác!");
            }

            // Bước 2: Kiểm tra xem username đã tồn tại trong Database chưa
            if (userRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên đăng nhập này đã có người sử dụng!");
            }

            // Bước 3: Tạo tài khoản mới
            User newUser = new User();
            newUser.setFullName(request.getFullName());
            newUser.setUsername(request.getUsername());
            newUser.setPasswordHash(passwordEncoder.encode(request.getPassword())); // Phải băm mật khẩu trước khi lưu
            newUser.setRole("ADMIN"); // Mặc định ai đăng ký cũng mang quyền Thầy Cô bình thường

            userRepository.save(newUser);
            return ResponseEntity.ok("Đăng ký tài khoản thành công!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private final String token;
        private final String fullName;
        private final String role;
    }

    @Data
    public static class RegisterRequest {
        private String fullName;
        private String username;
        private String password;
        private String secretCode;
    }
}