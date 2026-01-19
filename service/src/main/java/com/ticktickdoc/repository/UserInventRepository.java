package com.ticktickdoc.repository;

import com.ticktickdoc.model.entity.UserInventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInventRepository extends JpaRepository<UserInventModel, Long> {

    Optional<UserInventModel> findByInventId(String inventId);
}
