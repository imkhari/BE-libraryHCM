package com.school.library.controller;

import com.school.library.entity.Category;
import com.school.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*") // Mở cửa cho React.js sau này có thể gọi vào không bị lỗi CORS
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // API lấy tất cả danh mục
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // API lấy các danh mục gốc (không có parent) để làm Menu chính
    @GetMapping("/roots")
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }
}
