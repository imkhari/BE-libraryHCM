package com.school.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_role", nullable = false)
    private String visitorRole; // Lưu: TEACHER, STUDENT, GUEST, hoặc PARTY_MEMBER

    @Column(name = "visited_at", updatable = false)
    private LocalDateTime visitedAt = LocalDateTime.now();
}
