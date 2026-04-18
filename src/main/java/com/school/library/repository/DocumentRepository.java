package com.school.library.repository;

import com.school.library.entity.Document;
import com.school.library.dto.DocumentSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readType, d.description) " +
            "FROM Document d WHERE d.category.id = :categoryId")
    Page<DocumentSummaryDTO> findSummaryByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<DocumentSummaryDTO> searchSummaryByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d " +
            "WHERE LOWER(d.author) IN ('hồ chí minh', 'nguyễn ái quốc', 'x.y.z (hồ chí minh)') " +
            "AND (d.readType != com.school.library.enums.ReadType.HTML OR d.readType IS NULL) " +
            "AND LOWER(d.title) NOT LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findBooksByHoChiMinh(Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d " +
            "WHERE (LOWER(d.author) NOT IN ('hồ chí minh', 'nguyễn ái quốc', 'x.y.z (hồ chí minh)') OR d.author IS NULL) " +
            "AND (d.readType != com.school.library.enums.ReadType.HTML OR d.readType IS NULL) " +
            "AND LOWER(d.title) NOT LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findBooksAboutHoChiMinh(Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d " +
            "WHERE LOWER(d.title) LIKE '%toàn tập%'")
    Page<DocumentSummaryDTO> findToanTap(Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d " +
            "WHERE d.readType = com.school.library.enums.ReadType.HTML")
    Page<DocumentSummaryDTO> findArticles(Pageable pageable);

    @Query("SELECT new com.school.library.dto.DocumentSummaryDTO(d.id, d.title, d.author, d.coverImageUrl, d.slug, d.readTyped.description) " +
            "FROM Document d " +
            "WHERE LOWER(d.title) LIKE '%thơ%' OR LOWER(d.title) LIKE '%nhật ký%' OR LOWER(d.title) LIKE '%cảnh khuya%'")
    Page<DocumentSummaryDTO> findPoetry(Pageable pageable);
}