package com.school.library.dto;

import com.school.library.enums.ReadType;
import lombok.Data;

import java.util.List;

@Data
public class DocumentDTO {
    private Long id;
    private String title;
    private String author;
    private String publisherYear;
    private String coverImageUrl;
    private String slug;

    // Thuộc tính mới
    private ReadType readType;
    private String description;
    private String content;
    private String pdfUrl;

    // Danh sách các chương
    private List<ChapterDTO> chapters;
}