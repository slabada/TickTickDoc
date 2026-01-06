package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.exception.DocumentException;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.model.DocumentModel;
import com.ticktickdoc.repository.DocumentRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final SecurityUtil securityUtil;

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UserService userService;

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
        document.setLinkAuthor(id);
        DocumentModel save = documentRepository.save(document);
        return documentMapper.toDomain(save);
    }

    @Override
    @Transactional
    public DocumentDomain updateDocument(Long id, DocumentDomain documentDomain) {
        DocumentDomain document = getDocumentById(id);
        documentMapper.updateDocument(document, documentDomain);
        return document;
    }

    @Override
    public Page<DocumentDomain> getAllDocumentByAuthors(Pageable pageable) {
        List<Long> authorsIds = new LinkedList<>();
        Long id = securityUtil.getUserSecurity().getId();
        UserDomain user = userService.getUser(id);
        List<Long> subsidiaryUserIds = user.getLinkSubsidiaryUser().stream()
                .map(UserDomain::getId)
                .toList();
        authorsIds.add(id);
        authorsIds.addAll(subsidiaryUserIds);
        Page<DocumentModel> documents = documentRepository.findAllByLinkAuthorIn(authorsIds, pageable);
        return documents.map(documentMapper::toDomain);
    }
}
