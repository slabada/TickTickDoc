package com.ticktickdoc.controller;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.dto.DocumentDto;
import com.ticktickdoc.dto.ResponseDocumentDto;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.page.PageResponse;
import com.ticktickdoc.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Deprecated(since = "##До лучших времен")
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
    @Cacheable(value = "document", key = "#id")
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
    @CacheEvict(value = "document", key = "#id")
    public void deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocumentById(id);
    }
}
