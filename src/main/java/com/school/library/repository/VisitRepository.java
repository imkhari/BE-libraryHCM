package com.school.library.repository;

import com.school.library.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Đếm tổng số lượt truy cập theo vai trò (STUDENT, TEACHER, GUEST)
    long countByVisitorRole(String visitorRole);
}