package com.ticktickdoc.service;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.exception.DocumentException;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.model.DocumentModel;
import com.ticktickdoc.notification.domain.NotificationDocumentDomain;
import com.ticktickdoc.notification.domain.NotificationDomain;
import com.ticktickdoc.repository.DocumentRepository;
import com.ticktickdoc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final SecurityUtil securityUtil;

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UserService userService;

    private final KafkaTemplate<String, NotificationDomain> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

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
        DocumentModel document = documentRepository.findById(id)
                .orElseThrow(DocumentException.NonDocumentException::new);
        DocumentDomain domain = documentMapper.toDomain(document);
        documentMapper.updateDocument(domain, documentDomain);
        return domain;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void deleteDocumentById(Long id) {
        if(documentRepository.existsById(id)){
            documentRepository.deleteById(id);
        }
    }

    @Scheduled(cron = "0 0 12 * * *")
    private void getDocumentFotNotification() {
        long notificationDay = 3L;
        List<DocumentModel> documents = documentRepository.findAllByDateExecution(LocalDate.now().plusDays(notificationDay));

        Map<Long, List<DocumentModel>> documentsByAuthor = documents.stream()
                .collect(Collectors.groupingBy(DocumentModel::getLinkAuthor));

        for (Map.Entry<Long, List<DocumentModel>> entry : documentsByAuthor.entrySet()) {
            Long authorId = entry.getKey();
            List<DocumentModel> authorDocuments = entry.getValue();

            UserDomain user = userService.getUser(authorId);

            List<NotificationDocumentDomain> notificationDocumentDomains = new LinkedList<>();
            for (var document : authorDocuments) {
                NotificationDocumentDomain notification = new NotificationDocumentDomain();
                notification.setName(document.getName());
                notification.setDateExecution(document.getDateExecution());
                notificationDocumentDomains.add(notification);
            }

            NotificationDomain notification = new NotificationDomain();
            notification.setEmail(user.getEmail());
            notification.setDocument(notificationDocumentDomains);

            kafkaTemplate.send(topicName, notification);
        }
    }
}
