package com.ticktickdoc.notification.repository;

import com.ticktickdoc.notification.model.ProcessEventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessEventRepository extends JpaRepository<ProcessEventModel, Long> {
    Optional<ProcessEventModel> findByProcessEventId(UUID processEventId);
}
