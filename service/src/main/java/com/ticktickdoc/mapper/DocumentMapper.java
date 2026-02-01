package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.DocumentDomain;
import com.ticktickdoc.dto.DocumentDto;
import com.ticktickdoc.dto.ResponseDocumentDto;
import com.ticktickdoc.model.entity.DocumentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DocumentMapper {

    DocumentModel toModel(DocumentDomain documentDomain);

    @Mapping(target = "file", ignore = true)
    DocumentDomain toDomain(DocumentModel documentModel);

    @Mapping(target = "linkFileId", ignore = true)
    @Mapping(target = "linkAuthorId", ignore = true)
    DocumentDomain toDomain(DocumentDto documentDto);

    @Mapping(target = "linkFileId", ignore = true)
    @Mapping(target = "linkAuthorId", ignore = true)
    @Mapping(target = "id", ignore = true)
    DocumentDomain toDomain(ResponseDocumentDto documentDto);

    DocumentDto toDto(DocumentDomain documentDomain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "linkAuthorId", ignore = true)
    void updateDocument(DocumentDomain oldDocument, @MappingTarget DocumentDomain newDocument);
}
