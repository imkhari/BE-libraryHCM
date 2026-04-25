package com.school.library.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tts")
@CrossOrigin("*")
public class TtsController {

    @Value("${fpt.ai.api.key}")
    private String apiKey;

    @PostMapping("/synthesize")
    public ResponseEntity<?> synthesize(@RequestBody Map<String, String> request) {
        String text = request.get("text");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // Cấu hình Header chuẩn theo tài liệu V5 của FPT
        headers.set("api-key", apiKey);
        headers.set("speed", "");
        headers.set("voice", "leminh"); // 🌟 Đã chốt giọng Lê Minh ở đây

        // Đóng gói văn bản và header
        HttpEntity<String> entity = new HttpEntity<>(text, headers);

        try {
            // 🌟 Gọi đúng đường dẫn V5 như trong ảnh bạn chụp
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.fpt.ai/hmi/tts/v5", entity, Map.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi kết nối FPT AI: " + e.getMessage()));
        }
    }
}
