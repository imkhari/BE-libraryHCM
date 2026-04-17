package com.school.library.repository;

import com.school.library.entity.Document;
import com.school.library.dto.DocumentSummaryDTO; // Import DTO mới tạo
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Lấy danh sách tài liệu theo Category ID (chỉ lấy các trường cơ bản làm bìa sách)
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d WHERE d.category.id = :categoryId")
    Page<DocumentSummaryDTO> findSummaryByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Tìm kiếm sách theo tiêu đề, không phân biệt hoa thường
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<DocumentSummaryDTO> searchSummaryByTitle(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Tác phẩm CỦA Hồ Chí Minh
     * Điều kiện: Tác giả là Bác, không phải dạng HTML (bài báo), không phải bộ Toàn tập
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d " +
            "WHERE LOWER(d.author) IN ('hồ chí minh', 'nguyễn ái quốc', 'x.y.z') " +
            "AND d.readType != 'HTML' " +
            "AND LOWER(d.title) NOT LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findBooksByHoChiMinh(Pageable pageable);

    /**
     * Tác phẩm VỀ Hồ Chí Minh
     * Điều kiện: Tác giả KHÔNG phải là Bác (hoặc null), không phải bài báo, không phải Toàn tập
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d " +
            "WHERE (LOWER(d.author) NOT IN ('hồ chí minh', 'nguyễn ái quốc', 'x.y.z') OR d.author IS NULL) " +
            "AND d.readType != 'HTML' " +
            "AND LOWER(d.title) NOT LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findBooksAboutHoChiMinh(Pageable pageable);

    /**
     * Hồ Chí Minh Toàn Tập
     * Điều kiện: Trong tiêu đề có chữ "toàn tập"
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d " +
            "WHERE LOWER(d.title) LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findToanTap(Pageable pageable);

    /**
     * Những bài báo của Hồ Chí Minh
     * Điều kiện: readType là HTML
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d " +
            "WHERE d.readType = 'HTML'")
    Page<DocumentSummaryDTO> findArticles(Pageable pageable);

    /**
     * Thơ Hồ Chí Minh
     * Tạm thời lọc theo các từ khóa xuất hiện trong tiêu đề (Nhật ký trong tù, Thơ...)
     */
    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug) " +
            "FROM Document d " +
            "WHERE LOWER(d.title) LIKE '%thơ%' OR LOWER(d.title) LIKE '%nhật ký%' OR LOWER(d.title) LIKE '%cảnh khuya%'")
    Page<DocumentSummaryDTO> findPoetry(Pageable pageable);
}