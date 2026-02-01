package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.exception.DocumentException;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.model.entity.FileModel;
import com.ticktickdoc.repository.DocumentRepository;
import com.ticktickdoc.repository.FileRepository;
import com.ticktickdoc.storage.service.FileStorageService;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final SecurityUtil securityUtil;

    private final DocumentRepository documentRepository;
    private final UserChildService userChildService;
    private final DocumentMapper documentMapper;
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public DocumentDomain getDocumentById(Long id) {
        DocumentModel document = documentRepository.findById(id)
                .orElseThrow(DocumentException.NonDocumentException::new);
        return documentMapper.toDomain(document);
    }

    @Override
    @Transactional
    public DocumentDomain createDocument(DocumentDomain documentDomain) {
        DocumentModel document = documentMapper.toModel(documentDomain);
        Long id = securityUtil.getUserSecurity().getId();
        document.setLinkAuthorId(id);
        DocumentModel save = documentRepository.save(document);
        return documentMapper.toDomain(save);
    }

    @Override
    @Transactional
    public DocumentDomain updateDocument(Long id, DocumentDomain documentDomain) {
        DocumentModel document = documentRepository.findById(id)
                .orElseThrow(DocumentException.NonDocumentException::new);
        DocumentDomain domain = documentMapper.toDomain(document);
        documentMapper.updateDocument(documentDomain, domain);
        DocumentModel model = documentMapper.toModel(domain);
        DocumentModel save = documentRepository.save(model);
        return documentMapper.toDomain(save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDomain> getAllDocumentByAuthors(Pageable pageable) {
        Long currentUserId = securityUtil.getUserSecurity().getId();
        List<Long> allAuthorIds = getAllAuthorIds(currentUserId);

        Page<DocumentModel> documents = documentRepository.findAllByLinkAuthorIdIn(allAuthorIds, pageable);

        List<Long> fileIds = documents.getContent().stream()
                .map(DocumentModel::getLinkFileId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> fileNamesById;
        if (!fileIds.isEmpty()) {
            List<FileModel> files = fileRepository.findAllById(fileIds);
            fileNamesById = files.stream()
                    .collect(Collectors.toMap(
                            FileModel::getId,
                            FileModel::getOriginalFileName,
                            (existing, replacement) -> existing
                    ));
        } else {
            fileNamesById = new HashMap<>();
        }

        return documents.map(document -> {
            DocumentDomain domain = documentMapper.toDomain(document);

            if (document.getLinkFileId() != null) {
                String fileName = fileNamesById.get(document.getLinkFileId());
                domain.setFile(fileName);
            }

            return domain;
        });
    }

    private List<Long> getAllAuthorIds(Long userId) {
        List<Long> authorIds = new ArrayList<>();
        authorIds.add(userId);

        List<Long> childUserIds = userChildService.findAllChildUserByUserId(userId);
        if (childUserIds != null && !childUserIds.isEmpty()) {
            authorIds.addAll(childUserIds);
        }

        return authorIds;
    }

    @Override
    @Transactional
    public void deleteDocumentById(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            fileRepository.findByLinkDocument(id).ifPresent(
                    f -> {
                        fileStorageService.deleteFile(f.getFileName());
                        fileRepository.deleteById(f.getId());
                    }
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentModel> findAllByDateExecution(LocalDate localDate) {
        return documentRepository.findAllByDateExecution(localDate);
    }
}
