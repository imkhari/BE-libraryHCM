package com.school.library.dto;

import com.school.library.enums.ReadType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSummaryDTO {
    private Long id;
    private String title;
    private String author;
    private String coverImageUrl;
    private String slug;
    private ReadType readType;
    private String description;
}
