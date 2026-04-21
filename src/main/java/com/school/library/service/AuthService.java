package com.school.library.service;

import com.school.library.entity.User;
import com.school.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Công cụ mã hóa/giải mã Bcrypt

    public User authenticate(String username, String rawPassword) {
        // Tìm user trong Database
        User user = userRepository.findByUsername(username);

        // Kiểm tra user có tồn tại và mật khẩu gõ vào có khớp với mã băm trong DB không
        if (user != null && passwordEncoder.matches(rawPassword, user.getPasswordHash())) {

            // Nếu đúng -> Cập nhật thời gian đăng nhập cuối
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user); // Lưu lại vào DB

            return user;
        }

        // Nếu sai -> Quăng lỗi
        throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không chính xác!");
    }
}
