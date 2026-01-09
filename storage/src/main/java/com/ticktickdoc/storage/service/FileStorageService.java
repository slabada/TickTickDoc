package com.ticktickdoc.storage.service;

import com.ticktickdoc.storage.domain.FileDomain;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileDomain uploadFile(MultipartFile file);

    Resource downloadFile(String filename);

    void deleteFile(String fileName);
}
