package com.school.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_number")
    private Integer chapterNumber;

    @Column(name = "chapter_title")
    private String chapterTitle;

    // Dùng columnDefinition = "TEXT" để PostgreSQL lưu được văn bản rất dài
    @Column(columnDefinition = "TEXT")
    private String content;

    // Quan hệ Nhiều-Một: Nhiều chương thuộc về 1 cuốn sách
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    private Document document;
}
