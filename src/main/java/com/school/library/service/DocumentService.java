package com.school.library.service;

import com.school.library.dto.ChapterDTO;
import com.school.library.dto.DocumentDTO;
import com.school.library.dto.DocumentSummaryDTO;
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

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public DocumentDTO getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + id));
        return mapToDTO(document);
    }

    public Page<DocumentSummaryDTO> getAllDocumentsSummary(Pageable pageable) {
        return documentRepository.findAll(pageable).map(this::mapToSummaryDTO);
    }

    public Page<DocumentSummaryDTO> getDocumentsByCategoryType(String type, Pageable pageable) {
        switch (type) {
            case "cua-ho-chi-minh":
                return documentRepository.findBooksByHoChiMinh(pageable);
            case "tac-pham-ve-ho-chi-minh":
                return documentRepository.findBooksAboutHoChiMinh(pageable);
            case "ho-chi-minh-toan-tap":
                return documentRepository.findToanTap(pageable);
            case "bai-bao":
                return documentRepository.findArticles(pageable);
            case "tho-ho-chi-minh":
            case "nhat-ky-trong-tu":
                return documentRepository.findPoetry(pageable);
            default:
                return documentRepository.findBooksAboutHoChiMinh(pageable);
        }
    }

    public Page<DocumentSummaryDTO> searchDocuments(String keyword, Pageable pageable) {
        return documentRepository.searchSummaryByTitle(keyword, pageable);
    }

    public Page<DocumentSummaryDTO> getDocumentsByCategoryId(Long categoryId, Pageable pageable) {
        return documentRepository.findSummaryByCategoryId(categoryId, pageable);
    }

    private DocumentDTO mapToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setAuthor(document.getAuthor());
        dto.setPublisherYear(String.valueOf(document.getPublisherYear()));
        dto.setCoverImageUrl(document.getCoverImageUrl());
        dto.setSlug(document.getSlug());
        dto.setReadType(document.getReadType());
        dto.setDescription(document.getDescription());
        dto.setContent(document.getContent());
        dto.setPdfUrl(document.getPdfUrl());

        if (document.getChapters() != null && !document.getChapters().isEmpty()) {
            dto.setChapters(document.getChapters().stream()
                    .map(this::mapChapterToDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setChapters(new ArrayList<>());
        }

        return dto;
    }

    private DocumentSummaryDTO mapToSummaryDTO(Document document) {
        return new DocumentSummaryDTO(
                document.getId(),
                document.getTitle(),
                document.getAuthor(),
                document.getCoverImageUrl(),
                document.getSlug()
        );
    }

    private ChapterDTO mapChapterToDTO(Chapter chapter) {
        ChapterDTO dto = new ChapterDTO();
        dto.setId(chapter.getId());
        dto.setChapterNumber(chapter.getChapterNumber());
        dto.setChapterTitle(chapter.getChapterTitle());
        dto.setContent(chapter.getContent());
        return dto;
    }
}