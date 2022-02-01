package org.fgai4h.ap.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("select u from UserEntity u where u.idpID = :idpID")
    public UserEntity findByIdpId(@Param("idpID") String idpID);
}
