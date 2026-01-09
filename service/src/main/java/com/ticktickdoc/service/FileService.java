package com.ticktickdoc.service;

import com.ticktickdoc.storage.domain.FileDomain;
import com.ticktickdoc.storage.domain.FileDownloadDomain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    FileDomain upload(Long id, MultipartFile file);

    FileDownloadDomain download(Long id);

    List<FileDomain> filesList(Long documentId);

    void delete(Long id);
}
