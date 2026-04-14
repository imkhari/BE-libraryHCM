package com.school.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.library.enums.ReadType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    private String author;

    @Column(name = "publisher_year")
    private Integer publisherYear;

    @Column(name = "cover_image_url", length = 2000)
    private String coverImageUrl;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_type")
    private ReadType readType;

    @Column(columnDefinition = "TEXT")
    private String content; // Dùng cho loại HTML

    @Column(name = "pdf_url")
    private String pdfUrl;  // Dùng cho loại PDF

    // Quan hệ Một-Nhiều: 1 Sách có nhiều Chương
    // cascade = CascadeType.ALL: Khi xóa sách thì xóa luôn các chương
    @JsonIgnore
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();
}