package com.school.library.dto;

import lombok.Data;

@Data
public class ChapterDTO {
    private Long id;
    private Integer chapterNumber;
    private String chapterTitle;
    private String content;
    // Không chứa đối tượng Document ở đây để tránh lặp vô tận
}
