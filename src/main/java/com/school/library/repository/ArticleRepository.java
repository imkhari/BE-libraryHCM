package com.school.library.repository;

import com.school.library.dto.ArticleSummaryDTO;
import com.school.library.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    long countByCategory(String category);

    // Lấy tất cả bài viết của 1 chuyên mục, sắp xếp mới nhất lên đầu
    List<Article> findByCategoryOrderByCreatedAtDesc(String category);

    // Lấy toàn bộ bài viết trên hệ thống, sắp xếp mới nhất lên đầu
    List<Article> findAllByOrderByCreatedAtDesc();

    @Query("SELECT new com.school.library.dto.ArticleSummaryDTO(a.id, a.title, a.category, a.author.fullName, a.views, a.createdAt, a.thumbnailUrl, a.snippet) " +
            "FROM Article a ORDER BY a.createdAt DESC")
    List<ArticleSummaryDTO> findAllSummaries();

    @Query("SELECT new com.school.library.dto.ArticleSummaryDTO(a.id, a.title, a.category, a.author.fullName, a.views, a.createdAt, a.thumbnailUrl, a.snippet) " +
            "FROM Article a WHERE a.category = :category ORDER BY a.createdAt DESC")
    List<ArticleSummaryDTO> findSummariesByCategory(@Param("category") String category);
}
