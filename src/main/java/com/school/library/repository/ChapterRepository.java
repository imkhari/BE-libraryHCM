package com.school.library.repository;

import com.school.library.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    // Tìm tất cả các chương của 1 cuốn sách và sắp xếp theo số thứ tự chương (từ 1, 2, 3...)
    List<Chapter> findByDocumentIdOrderByChapterNumberAsc(Long documentId);
}
