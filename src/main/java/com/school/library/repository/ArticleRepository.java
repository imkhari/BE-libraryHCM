package com.school.library.repository;

import com.school.library.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    long countByCategory(String category);

    // Lấy tất cả bài viết của 1 chuyên mục, sắp xếp mới nhất lên đầu
    List<Article> findByCategoryOrderByCreatedAtDesc(String category);

    // Lấy toàn bộ bài viết trên hệ thống, sắp xếp mới nhất lên đầu
    List<Article> findAllByOrderByCreatedAtDesc();
}
