package com.school.library.service;

import com.school.library.entity.Article;
import com.school.library.entity.User;
import com.school.library.repository.ArticleRepository;
import com.school.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public List<Article> getArticles(String category) {
        if (category == null || category.isEmpty() || category.equals("ALL")) {
            return articleRepository.findAllByOrderByCreatedAtDesc();
        }
        return articleRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    public Article createArticle(String title, String content, String category, String authorUsername) {
        // Tìm tác giả bằng username (lấy từ JWT Token)
        User author = userRepository.findByUsername(authorUsername);
        if (author == null) {
            throw new RuntimeException("Không tìm thấy thông tin tác giả hợp lệ!");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCategory(category);
        article.setAuthor(author);

        return articleRepository.save(article);
    }
}
