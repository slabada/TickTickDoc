package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.exception.DocumentException;
import com.ticktickdoc.exception.FileException;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.mapper.FileMapper;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.model.entity.FileModel;
import com.ticktickdoc.repository.FileRepository;
import com.ticktickdoc.storage.domain.FileDomain;
import com.ticktickdoc.storage.domain.FileDownloadDomain;
import com.ticktickdoc.storage.service.FileStorageService;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final SecurityUtil securityUtil;

    private final FileStorageService fileStorageService;
    private final DocumentService documentService;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final DocumentMapper documentMapper;

    @Override
    @Transactional
    public FileDomain upload(Long documentId, MultipartFile file) {
        Long currentId = securityUtil.getUserSecurity().getId();
        DocumentDomain documentById = documentService.getDocumentById(documentId);
        if(!documentById.getLinkAuthorId().equals(currentId)) {
            throw new FileException.BadRequestAddFileForDocumentException();
        }
        DocumentModel document = documentMapper.toModel(documentById);
        FileDomain fileDomain = fileStorageService.uploadFile(file);
        FileModel model = fileMapper.toModel(fileDomain);
        model.setLinkDocument(document.getId());
        FileModel save = fileRepository.save(model);
        return fileMapper.toDomain(save);
    }

    @Override
    public FileDownloadDomain download(String fileName) {
        FileModel file = fileRepository.findByOriginalFileName(fileName)
                .orElseThrow(FileException.NonFileException::new);
        Resource resource = fileStorageService.downloadFile(file.getFileName());
        return new FileDownloadDomain(
                resource,
                file.getOriginalFileName()
        );
    }

    @Override
    public FileDomain getFileByDocumentId(Long documentId) {
        FileModel file = fileRepository.findByLinkDocument(documentId)
                .orElseThrow(DocumentException.NonDocumentException::new);
        return fileMapper.toDomain(file);
    }

    @Override
    public void delete(Long id) {
        FileModel file = fileRepository.findById(id)
                .orElseThrow(FileException.NonFileException::new);
        fileStorageService.deleteFile(file.getFileName());
        fileRepository.delete(file);
    }
}
