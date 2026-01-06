package com.ticktickdoc.repository;

import com.ticktickdoc.model.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {

    Page<DocumentModel> findAllByLinkAuthorIn(List<Long> id, Pageable pageable);

}
