package com.school.library.repository;

import com.school.library.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Spring Data JPA sẽ tự động tạo code cho các hàm cơ bản.
    // Mình có thể viết thêm các hàm custom ở đây, ví dụ tìm danh mục gốc:
    List<Category> findByParentIsNull();
}
