package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {

    DocumentDomain getDocumentById(Long id);

    DocumentDomain createDocument(DocumentDomain documentDomain);

    DocumentDomain updateDocument(Long id, DocumentDomain documentDomain);

    Page<DocumentDomain> getAllDocumentByAuthors(Pageable pageable);

    void deleteDocumentById(Long id);
}
