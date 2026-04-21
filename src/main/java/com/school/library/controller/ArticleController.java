package com.school.library.controller;

import com.school.library.entity.Article;
import com.school.library.service.ArticleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // GET /api/v1/articles?category=TIN_TUC
    @GetMapping
    public ResponseEntity<List<Article>> getArticles(
            @RequestParam(required = false, defaultValue = "ALL") String category) {
        return ResponseEntity.ok(articleService.getArticles(category));
    }

    // POST /api/v1/articles
    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleRequest request) {
        try {
            // TẠM THỜI gán cứng username để test API.
            // Sau này khi có Security, ta sẽ lấy username động từ Token của người gửi.
            String authorUsername = "thaiphien_admin";

            Article newArticle = articleService.createArticle(
                    request.getTitle(),
                    request.getContent(),
                    request.getCategory(),
                    authorUsername
            );
            return ResponseEntity.ok(newArticle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi đăng bài: " + e.getMessage());
        }
    }

    @Data
    public static class ArticleRequest {
        private String title;
        private String content;
        private String category;
    }
}