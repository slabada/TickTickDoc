package com.ticktickdoc.repository;

import com.ticktickdoc.model.entity.OutboxModel;
import feign.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxModel, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select o from OutboxModel o
        where o.send = :send
    """)
    List<OutboxModel> findForSend(@Param("send") Boolean send);
}
