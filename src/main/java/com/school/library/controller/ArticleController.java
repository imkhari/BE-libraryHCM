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

    // GET /api/articles/1 (Lấy chi tiết 1 bài)
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articleService.getArticleById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        try {
            Article updatedArticle = articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(updatedArticle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return ResponseEntity.ok("Đã xóa bài viết thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }

    @Data
    public static class ArticleRequest {
        private String title;
        private String content;
        private String category;
    }
}