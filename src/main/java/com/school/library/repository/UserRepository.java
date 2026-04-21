package com.school.library.repository;

import com.school.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm tài khoản bằng username (Dùng lúc Đăng nhập)
    User findByUsername(String username);

    // Kiểm tra xem username đã tồn tại chưa (Hữu ích nếu sau này có tính năng thêm giáo viên mới)
    boolean existsByUsername(String username);
}
