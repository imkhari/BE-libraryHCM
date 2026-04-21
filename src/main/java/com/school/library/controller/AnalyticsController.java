package com.school.library.controller;

import com.school.library.service.AnalyticsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // POST /api/v1/analytics/visit
    // Gọi mỗi khi có người chọn vai trò (Học sinh/Giáo viên/Khách) trên web
    @PostMapping("/visit")
    public ResponseEntity<?> logVisit(@RequestBody VisitRequest request) {
        try {
            analyticsService.logVisit(request.getRole());
            return ResponseEntity.ok("Ghi nhận lượt truy cập thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi hệ thống ghi nhận");
        }
    }

    // GET /api/v1/analytics/summary
    // Gọi để lấy số liệu vẽ Dashboard cho thầy cô
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getSummary() {
        return ResponseEntity.ok(analyticsService.getDashboardStats());
    }

    @Data
    public static class VisitRequest {
        private String role; // TEACHER, STUDENT, GUEST
    }
}
