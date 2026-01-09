package com.ticktickdoc.mapper;

import com.ticktickdoc.dto.FileDto;
import com.ticktickdoc.model.FileModel;
import com.ticktickdoc.storage.domain.FileDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "document", ignore = true)
    @Mapping(target = "id", ignore = true)
    FileModel toModel(FileDomain domain);

    FileDomain toDomain(FileModel model);

    List<FileDomain> toDomain(List<FileModel> model);

    FileDto toDto(FileDomain domain);

    List<FileDto> toDto(List<FileDomain> domain);
}
