package com.school.library.service;

import com.school.library.entity.Visit;
import com.school.library.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VisitRepository visitRepository;

    // Ghi nhận ai đó vừa vào web
    public void logVisit(String role) {
        Visit visit = new Visit();
        visit.setVisitorRole(role);
        visitRepository.save(visit);
    }

    // Lấy tổng hợp dữ liệu cho Dashboard
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("STUDENT", visitRepository.countByVisitorRole("STUDENT"));
        stats.put("TEACHER", visitRepository.countByVisitorRole("TEACHER"));
        stats.put("GUEST", visitRepository.countByVisitorRole("GUEST"));
        stats.put("TOTAL", visitRepository.count()); // Tổng số tất cả các lượt truy cập

        return stats;
    }
}