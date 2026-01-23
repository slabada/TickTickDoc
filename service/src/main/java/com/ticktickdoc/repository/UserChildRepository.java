package com.ticktickdoc.repository;

import com.ticktickdoc.model.entity.UserChildModel;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChildRepository extends JpaRepository<UserChildModel, Long> {

    boolean existsByParentUserId(Long parentUserId);

    boolean existsByChildUserIdAndParentUserId(Long childUserId, Long parentUserId);

    boolean existsByChildUserId(Long childUserId);

    UserChildModel removeByParentUserIdAndChildUserId(Long parentUserId, Long childUserId);

    @Query("""
    SELECT u.id as userId,
           u.fullName as fullName,
           u.email as email,
           u.telegram as telegram,
           (SELECT COUNT(d.id)
            FROM DocumentModel d
            WHERE d.linkAuthorId = u.id) as countDocument
    FROM UserModel u
    LEFT JOIN UserChildModel uc ON uc.childUserId = u.id
    WHERE uc.parentUserId = :currentId
    """)
    List<UserChildDocumentProjection> findUserWithDocumentCount(@Param("currentId") Long currentId);

    @Query("""
            SELECT u.childUserId
            FROM UserChildModel u
            WHERE u.parentUserId = :parentId
            """)
    List<Long> findAllByParentUserId(@Param("parentId") Long parentId);
}
