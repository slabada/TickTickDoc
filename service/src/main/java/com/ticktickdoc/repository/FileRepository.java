package com.ticktickdoc.repository;

import com.ticktickdoc.model.entity.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {

    List<FileModel> findAllByLinkDocument(Long documentId);
}
