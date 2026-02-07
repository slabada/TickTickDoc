package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.model.entity.FileModel;
import com.ticktickdoc.repository.DocumentRepository;
import com.ticktickdoc.repository.FileRepository;
import com.ticktickdoc.storage.service.FileStorageService;
import com.ticktickdoc.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserChildService userChildService;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private DocumentModel documentModel;
    private DocumentDomain documentDomain;
    private UserAdaptor userAdaptor;

    @BeforeEach
    void setUp() {
        documentModel = DocumentModel.builder()
                .id(1L)
                .linkAuthorId(10L)
                .linkFileId(100L)
                .build();

        documentDomain = DocumentDomain.builder()
                .id(1L)
                .build();

        var userDomain = UserDomain.builder()
                .id(10L)
                .build();

        userAdaptor = UserAdaptor.builder()
                .user(userDomain)
                .authorities(List.of())
                .build();
    }

    @Test
    void getDocumentById() {
        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(documentModel));

        when(documentMapper.toDomain(documentModel))
                .thenReturn(documentDomain);

        DocumentDomain result = documentService.getDocumentById(1L);

        assertEquals(1L, result.getId());
        verify(documentRepository).findById(1L);
    }

    @Test
    void createDocument() {
        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(documentMapper.toModel(documentDomain)).thenReturn(documentModel);
        when(documentRepository.save(documentModel)).thenReturn(documentModel);
        when(documentMapper.toDomain(documentModel)).thenReturn(documentDomain);

        DocumentDomain result = documentService.createDocument(documentDomain);

        assertNotNull(result);
        verify(documentRepository).save(documentModel);
        assertEquals(10L, documentModel.getLinkAuthorId());
    }

    @Test
    void updateDocument() {
        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(documentModel));

        when(documentMapper.toDomain(documentModel))
                .thenReturn(documentDomain);

        when(documentMapper.toModel(documentDomain))
                .thenReturn(documentModel);

        when(documentRepository.save(documentModel))
                .thenReturn(documentModel);

        when(documentMapper.toDomain(documentModel))
                .thenReturn(documentDomain);

        DocumentDomain result = documentService.updateDocument(1L, documentDomain);

        assertNotNull(result);
        verify(documentRepository).save(documentModel);
    }

    @Test
    void getAllDocumentByAuthors() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DocumentModel> page = new PageImpl<>(List.of(documentModel));

        FileModel fileModel = new FileModel();
        fileModel.setId(100L);
        fileModel.setOriginalFileName("file.pdf");

        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(userChildService.findAllChildUserByUserId(10L))
                .thenReturn(List.of(20L));

        when(documentRepository.findAllByLinkAuthorIdIn(anyList(), eq(pageable)))
                .thenReturn(page);

        when(documentMapper.toDomain(documentModel))
                .thenReturn(documentDomain);

        when(fileRepository.findAllById(List.of(100L)))
                .thenReturn(List.of(fileModel));

        Page<DocumentDomain> result = documentService.getAllDocumentByAuthors(pageable);

        assertEquals(1, result.getTotalElements());
        verify(documentRepository).findAllByLinkAuthorIdIn(anyList(), eq(pageable));
    }

    @Test
    void deleteDocumentById() {
        FileModel fileModel = new FileModel();
        fileModel.setId(100L);
        fileModel.setFileName("stored.pdf");

        when(documentRepository.existsById(1L)).thenReturn(true);
        when(fileRepository.findByLinkDocument(1L))
                .thenReturn(Optional.of(fileModel));

        documentService.deleteDocumentById(1L);

        verify(documentRepository).deleteById(1L);
        verify(fileStorageService).deleteFile("stored.pdf");
        verify(fileRepository).deleteById(100L);
    }

    @Test
    void findAllByDateExecution() {
        LocalDate date = LocalDate.now();

        when(documentRepository.findAllByDateExecution(date))
                .thenReturn(List.of(documentModel));

        List<DocumentModel> result = documentService.findAllByDateExecution(date);

        assertEquals(1, result.size());
        verify(documentRepository).findAllByDateExecution(date);
    }
}