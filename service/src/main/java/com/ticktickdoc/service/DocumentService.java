package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.model.entity.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {

    DocumentDomain getDocumentById(Long id);

    DocumentDomain createDocument(DocumentDomain documentDomain);

    DocumentDomain updateDocument(Long id, DocumentDomain documentDomain);

    Page<DocumentDomain> getAllDocumentByAuthors(Pageable pageable);

    void deleteDocumentById(Long id);

    List<DocumentModel> findAllByDateExecution(LocalDate localDate);
}
