package com.school.library.repository;

import com.school.library.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Lấy danh sách tài liệu theo Category ID (đã đổi sang Long)
    Page<Document> findByCategory_Id(Long categoryId, Pageable pageable);

    // Tìm kiếm sách theo tiêu đề, không phân biệt hoa thường (có phân trang)
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Document> searchByTitle(@Param("keyword") String keyword, Pageable pageable);
}