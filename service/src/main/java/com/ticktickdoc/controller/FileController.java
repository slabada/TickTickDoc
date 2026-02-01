package com.ticktickdoc.controller;

import com.ticktickdoc.dto.FileDto;
import com.ticktickdoc.mapper.FileMapper;
import com.ticktickdoc.service.FileService;
import com.ticktickdoc.storage.domain.FileDomain;
import com.ticktickdoc.storage.domain.FileDownloadDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> upload(@PathVariable("id") Long id, MultipartFile file) {
        FileDomain fileDomain = fileService.upload(id ,file);
        FileDto dto = fileMapper.toDto(fileDomain);
        return ResponseEntity.ok().body(dto);
    }

    @Deprecated
    @GetMapping("/file/{documentId}")
    public ResponseEntity<FileDto> getFileByDocumentId(@PathVariable("documentId") Long documentId) {
        FileDomain fileDomains = fileService.getFileByDocumentId(documentId);
        FileDto dto = fileMapper.toDto(fileDomains);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> download(@PathVariable("documentId") Long documentId) {
        FileDownloadDomain download = fileService.download(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + download.getOriginalName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(download.getResource());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        fileService.delete(id);
    }
}
