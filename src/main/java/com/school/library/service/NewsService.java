package com.school.library.service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    @Cacheable(value = "newsCache", key = "#category")
    public List<Map<String, String>> fetchNewsFromRSS(String category) {
        List<Map<String, String>> articles = new ArrayList<>();

        // Link RSS Báo Nhân Dân
        String rssUrl = "https://nhandan.vn/rss/xay-dung-dang-553.rss";

        // 🌟 DÙNG API TRUNG GIAN ĐỂ CHUYỂN RSS THÀNH JSON
        String apiUrl = "https://api.rss2json.com/v1/api.json?rss_url=" + rssUrl;

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Giữ lại Fake User-Agent cho chắc ăn
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // Dùng thư viện Jackson có sẵn của Spring Boot để đọc JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (int i = 0; i < Math.min(10, items.size()); i++) {
                    JsonNode item = items.get(i);
                    Map<String, String> article = new HashMap<>();

                    article.put("title", item.path("title").asText());
                    article.put("url", item.path("link").asText());
                    article.put("publishedAt", item.path("pubDate").asText());

                    // Xử lý mô tả và ảnh
                    String descriptionHtml = item.path("description").asText();
                    String cleanDescription = descriptionHtml.replaceAll("<.*?>", "").trim();

                    // RSS2JSON thường có sẵn trường 'thumbnail'
                    String imageUrl = item.path("thumbnail").asText();

                    // Nếu thumbnail rỗng, bóc tách ảnh từ mô tả
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = "https://via.placeholder.com/300x200?text=Bao+Nhan+Dan";
                        if (descriptionHtml.contains("<img ")) {
                            int srcStart = descriptionHtml.indexOf("src=\"") + 5;
                            int srcEnd = descriptionHtml.indexOf("\"", srcStart);
                            if(srcStart > 4 && srcEnd > srcStart) {
                                imageUrl = descriptionHtml.substring(srcStart, srcEnd);
                            }
                        }
                    }

                    article.put("image", imageUrl);
                    article.put("description", cleanDescription);

                    articles.add(article);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc RSS qua trung gian: " + e.getMessage());
        }

        return articles;
    }
}