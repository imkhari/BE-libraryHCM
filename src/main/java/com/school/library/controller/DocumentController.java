package com.school.library.controller;

import com.school.library.dto.DocumentDTO;
import com.school.library.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = "*", maxAge = 3600) // Cho phép React gọi API
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // API lấy danh sách sách (dùng cho trang chủ và Swiper)
    @GetMapping
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(documentService.getAllDocuments(pageable));
    }

    // API lấy chi tiết 1 cuốn sách (bao gồm cả các chương để đọc online)
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }
}