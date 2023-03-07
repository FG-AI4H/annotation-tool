package org.fgai4h.ap.domain.dataset.repository;

import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DatasetRepository extends JpaRepository<DatasetEntity, UUID> {


    @Query("SELECT r.dataset FROM DatasetRoleEntity r WHERE r.user.idpID = :userId")
    List<DatasetEntity> findAllByUserId(@Param("userId") String userId);
}
