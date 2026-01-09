package com.ticktickdoc.repository;

import com.ticktickdoc.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {

    List<FileModel> findAllByDocumentId(Long documentId);
}
