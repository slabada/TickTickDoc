package com.ticktickdoc.service;

import com.ticktickdoc.adaptor.UserAdaptor;
import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.mapper.DocumentMapper;
import com.ticktickdoc.mapper.FileMapper;
import com.ticktickdoc.model.entity.DocumentModel;
import com.ticktickdoc.model.entity.FileModel;
import com.ticktickdoc.repository.FileRepository;
import com.ticktickdoc.storage.domain.FileDomain;
import com.ticktickdoc.storage.domain.FileDownloadDomain;
import com.ticktickdoc.storage.service.FileStorageService;
import com.ticktickdoc.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private DocumentService documentService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Resource resource;

    @InjectMocks
    private FileServiceImpl fileService;

    private DocumentDomain documentDomain;
    private DocumentModel documentModel;
    private FileDomain fileDomain;
    private FileModel fileModel;
    private UserAdaptor userAdaptor;

    @BeforeEach
    void setUp() {
        documentDomain = DocumentDomain.builder()
                .id(1L)
                .linkAuthorId(10L)
                .build();

        documentModel = DocumentModel.builder()
                .id(1L)
                .build();

        fileDomain = FileDomain.builder()
                .id(100L)
                .build();

        fileModel = FileModel.builder()
                .id(100L)
                .fileName("stored.pdf")
                .originalFileName("original.pdf")
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
    void upload() {
        when(securityUtil.getUserSecurity()).thenReturn(userAdaptor);
        when(documentService.getDocumentById(1L)).thenReturn(documentDomain);
        when(documentMapper.toModel(documentDomain)).thenReturn(documentModel);

        when(fileStorageService.uploadFile(multipartFile))
                .thenReturn(fileDomain);

        when(fileMapper.toModel(fileDomain))
                .thenReturn(fileModel);

        when(fileRepository.save(fileModel))
                .thenReturn(fileModel);

        when(fileMapper.toDomain(fileModel))
                .thenReturn(fileDomain);

        FileDomain result = fileService.upload(1L, multipartFile);

        assertNotNull(result);
        verify(fileRepository).save(fileModel);
        verify(documentService).updateDocument(eq(1L), any(DocumentDomain.class));
    }

    @Test
    void download() {
        when(fileRepository.findByLinkDocument(1L))
                .thenReturn(Optional.of(fileModel));

        when(fileStorageService.downloadFile("stored.pdf"))
                .thenReturn(resource);

        FileDownloadDomain result = fileService.download(1L);

        assertEquals("original.pdf", result.getOriginalName());
        assertEquals(resource, result.getResource());
    }

    @Test
    void getFileByDocumentId() {
        when(fileRepository.findByLinkDocument(1L))
                .thenReturn(Optional.of(fileModel));

        when(fileMapper.toDomain(fileModel))
                .thenReturn(fileDomain);

        FileDomain result = fileService.getFileByDocumentId(1L);

        assertNotNull(result);
        verify(fileMapper).toDomain(fileModel);
    }

    @Test
    void delete() {
        when(fileRepository.findById(100L))
                .thenReturn(Optional.of(fileModel));

        fileService.delete(100L);

        verify(fileStorageService).deleteFile("stored.pdf");
        verify(fileRepository).delete(fileModel);
    }
}