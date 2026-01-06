package com.ticktickdoc.controller;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.dto.DocumentDto;
import com.ticktickdoc.dto.ResponseDocumentDto;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.page.PageResponse;
import com.ticktickdoc.service.DocumentService;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DocumentController {

    private final SecurityUtil securityUtil;

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
    public DocumentDto getDocumentById(@PathVariable("id") Long id){
        DocumentDomain documentById = documentService.getDocumentById(id);
        return documentMapper.toDto(documentById);
    }

    @PostMapping("/document")
    public DocumentDto createDocument(@RequestBody ResponseDocumentDto documentDto) {
        DocumentDomain domain = documentMapper.toDomain(documentDto);
        DocumentDomain document = documentService.createDocument(domain);
        return documentMapper.toDto(document);
    }

    @PutMapping("/document/{id}")
    public DocumentDto updateDocument(@PathVariable("id") Long id,@RequestBody ResponseDocumentDto documentDto) {
        DocumentDomain domain = documentMapper.toDomain(documentDto);
        DocumentDomain document = documentService.updateDocument(id, domain);
        return documentMapper.toDto(document);
    }
}
