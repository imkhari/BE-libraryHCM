package com.school.library.controller;

import com.school.library.entity.User;
import com.school.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // LẤY DANH SÁCH TẤT CẢ GIÁO VIÊN
    @GetMapping
    public ResponseEntity<?> getAllUsers(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());

        // Chặn cửa nếu user không tồn tại hoặc không phải Trùm cuối
        if (currentUser == null || !"SUPER_ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xem danh sách này!");
        }

        List<User> users = userRepository.findAll();
        // Ẩn password hash đi trước khi gửi về React để bảo mật
        users.forEach(u -> u.setPasswordHash(""));
        return ResponseEntity.ok(users);
    }

    // TÍNH NĂNG RESET MẬT KHẨU
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());

        if (currentUser == null || !"SUPER_ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Chỉ Quản trị viên cấp cao mới được phép reset mật khẩu!");
        }

        // Lưu ý: findById là hàm mặc định của Spring, nó LUÔN trả về Optional, nên chỗ này dùng .orElseThrow() là đúng chuẩn không bị lỗi
        User userToReset = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên này!"));

        // Cứu hộ: Ép mật khẩu về mặc định là Thaiphien@123
        userToReset.setPasswordHash(passwordEncoder.encode("Thaiphien@123"));
        userRepository.save(userToReset);

        return ResponseEntity.ok("Đã cấp lại mật khẩu mặc định (Thaiphien@123) cho Thầy/Cô " + userToReset.getFullName());
    }
}