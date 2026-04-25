package com.school.library.service;

import com.school.library.dto.ArticleSummaryDTO;
import com.school.library.entity.Article;
import com.school.library.entity.User;
import com.school.library.repository.ArticleRepository;
import com.school.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public List<ArticleSummaryDTO> getArticles(String category) {
        if (category == null || category.isEmpty() || category.equals("ALL")) {
            return articleRepository.findAllSummaries();
        }
        return articleRepository.findSummariesByCategory(category);
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
        article.setThumbnailUrl(extractThumbnail(content));
        article.setSnippet(extractSnippet(content));

        return articleRepository.save(article);
    }

    // Lấy chi tiết 1 bài viết theo ID
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết!"));
    }

    public Article updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + id));

        // Cập nhật các thông tin mới
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setCategory(articleDetails.getCategory());
        article.setAuthor(articleDetails.getAuthor());
        article.setCreatedAt(articleDetails.getCreatedAt());
        article.setThumbnailUrl(extractThumbnail(articleDetails.getContent()));
        article.setSnippet(extractSnippet(articleDetails.getContent()));
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + id));
        articleRepository.delete(article);
    }

    private String extractThumbnail(String htmlContent) {
        if (htmlContent == null || htmlContent.trim().isEmpty()) {
            return "https://tranhdaquy24h.com/public/upload/images/7ef5cf3972688e36d779.jpg";
        }

        // Regex mới: Quét mọi thẻ img và lấy chính xác nội dung bên trong src="..."
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        if (matcher.find()) {
            return matcher.group(1); // Trả về link ảnh (kể cả link Base64 siêu dài)
        }

        // Trả về ảnh mặc định nếu bài viết toàn chữ
        return "https://tranhdaquy24h.com/public/upload/images/7ef5cf3972688e36d779.jpg";
    }

    private String extractSnippet(String htmlContent) {
        if (htmlContent == null || htmlContent.isEmpty()) return "";

        // 1. Xóa toàn bộ thẻ HTML (<p>, <strong>, <img>...)
        String plainText = htmlContent.replaceAll("<[^>]*>", "");
        // 2. Xóa các ký tự trắng thừa (&nbsp;)
        plainText = plainText.replaceAll("&nbsp;", " ").trim();

        // 3. Cắt lấy 150 ký tự đầu tiên
        if (plainText.length() > 150) {
            return plainText.substring(0, 150) + "...";
        }
        return plainText;
    }
}
