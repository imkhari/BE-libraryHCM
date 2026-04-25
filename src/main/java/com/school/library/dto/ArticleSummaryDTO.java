package com.school.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummaryDTO {
    private Long id;
    private String title;
    private String category;
    private String authorName;
    private Integer views;
    private LocalDateTime createdAt;
    private String thumbnailUrl;
    private String snippet;
}
