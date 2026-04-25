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

        // 🌟 BẢO VỆ: Nếu text quá dài, FPT sẽ chặn. Ta chỉ lấy tối đa 4000 ký tự.
        if (text != null && text.length() > 4000) {
            text = text.substring(0, 4000) + "... (Hết phần nghe thử do bài viết quá dài)";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("speed", "");
        headers.set("voice", "leminh");

        // Đảm bảo gửi dạng chuỗi UTF-8 chuẩn
        headers.setContentType(org.springframework.http.MediaType.valueOf("text/plain; charset=utf-8"));
        HttpEntity<String> entity = new HttpEntity<>(text, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.fpt.ai/hmi/tts/v5", entity, Map.class);
            return ResponseEntity.ok(response.getBody());

        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            // 🌟 NÂNG CẤP: Nếu FPT báo lỗi (sai key, hết tiền, text dài...), in thẳng lỗi đó ra Log của Render để dễ đọc
            System.err.println("FPT AI TỪ CHỐI: " + e.getResponseBodyAsString());
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi từ FPT: " + e.getResponseBodyAsString()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi Server nội bộ: " + e.getMessage()));
        }
    }
}
