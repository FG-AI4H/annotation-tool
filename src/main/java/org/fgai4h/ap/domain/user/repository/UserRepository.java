package org.fgai4h.ap.domain.user.repository;

import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("select u from UserEntity u where u.idpId = :idpID")
    Optional<UserEntity> findByIdpId(@Param("idpID") String idpID);

}
