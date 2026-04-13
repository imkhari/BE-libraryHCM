package com.school.library.service;

import com.school.library.dto.ChapterDTO;
import com.school.library.dto.DocumentDTO;
import com.school.library.entity.Chapter;
import com.school.library.entity.Document;
import com.school.library.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.school.library.enums.ReadType;


@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    /**
     * 1. Lấy chi tiết 1 cuốn sách (dùng cho trang Đọc sách)
     */
    public DocumentDTO getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + id));

        return mapToDTO(document);
    }

    /**
     * 2. Lấy danh sách phân trang (dùng cho trang chủ hiển thị dạng lưới)
     */
    public Page<DocumentDTO> getAllDocuments(Pageable pageable) {
        Page<Document> documentPage = documentRepository.findAll(pageable);

        // Chuyển đổi từ Page<Document> sang Page<DocumentDTO>
        return documentPage.map(this::mapToDTO);
    }

    /**
     * HÀM PHỤ TRỢ: Chuyển đổi từ Entity (Document) sang DTO (DocumentDTO)
     */
    private DocumentDTO mapToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setAuthor(document.getAuthor());
        dto.setPublisherYear(String.valueOf(document.getPublisherYear()));
        dto.setCoverImageUrl(document.getCoverImageUrl());

        // Các trường phục vụ đọc online
        dto.setReadType(document.getReadType());
        dto.setDescription(document.getDescription());
        dto.setContent(document.getContent());
        dto.setPdfUrl(document.getPdfUrl());

        // Map danh sách Chapters sang ChapterDTOs (Nếu sách đó có chapter)
        if (document.getChapters() != null && !document.getChapters().isEmpty()) {
            List<ChapterDTO> chapterDTOs = document.getChapters().stream()
                    .map(this::mapChapterToDTO)
                    .collect(Collectors.toList());
            dto.setChapters(chapterDTOs);
        } else {
            dto.setChapters(new ArrayList<>());
        }

        return dto;
    }

    /**
     * HÀM PHỤ TRỢ: Chuyển đổi từ Entity (Chapter) sang DTO (ChapterDTO)
     */
    private ChapterDTO mapChapterToDTO(Chapter chapter) {
        ChapterDTO dto = new ChapterDTO();
        dto.setId(chapter.getId());
        dto.setChapterNumber(chapter.getChapterNumber());
        dto.setChapterTitle(chapter.getChapterTitle());
        dto.setContent(chapter.getContent());
        return dto;
    }
}
