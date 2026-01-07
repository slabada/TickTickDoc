package com.ticktickdoc.controller;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.dto.DocumentDto;
import com.ticktickdoc.dto.ResponseDocumentDto;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.page.PageResponse;
import com.ticktickdoc.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @GetMapping("/documents/me")
    public PageResponse<DocumentDto> getMyDocuments(Pageable pageable){
        Page<DocumentDomain> documents = documentService.getAllDocumentByAuthors(pageable);
        List<DocumentDto> content = documents.stream()
                .map(documentMapper::toDto)
                .toList();
        return new PageResponse<>(
                content,
                documents.getNumber(),
                documents.getSize(),
                documents.getTotalPages(),
                documents.getTotalElements()
        );
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable("id") Long id){
        DocumentDomain documentById = documentService.getDocumentById(id);
        DocumentDto dto = documentMapper.toDto(documentById);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/document")
    public ResponseEntity<DocumentDto> createDocument(@RequestBody ResponseDocumentDto documentDto) {
        DocumentDomain domain = documentMapper.toDomain(documentDto);
        DocumentDomain document = documentService.createDocument(domain);
        DocumentDto dto = documentMapper.toDto(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/document/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable("id") Long id, @RequestBody ResponseDocumentDto documentDto) {
        DocumentDomain domain = documentMapper.toDomain(documentDto);
        DocumentDomain document = documentService.updateDocument(id, domain);
        DocumentDto dto = documentMapper.toDto(document);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/document/{id}")
    public void deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocumentById(id);
    }
}
