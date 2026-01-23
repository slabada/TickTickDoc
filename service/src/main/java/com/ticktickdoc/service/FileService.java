package com.ticktickdoc.service;

import com.ticktickdoc.storage.domain.FileDomain;
import com.ticktickdoc.storage.domain.FileDownloadDomain;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileDomain upload(Long id, MultipartFile file);

    FileDownloadDomain download(String fileName);

    FileDomain getFileByDocumentId(Long documentId);

    void delete(Long id);
}
