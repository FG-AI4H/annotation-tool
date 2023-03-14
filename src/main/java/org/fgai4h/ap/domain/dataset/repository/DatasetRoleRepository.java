package org.fgai4h.ap.domain.dataset.repository;

import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DatasetRoleRepository extends JpaRepository<DatasetRoleEntity, UUID> {

    @Query("SELECT r FROM DatasetRoleEntity r WHERE r.dataset.datasetUUID = :datasetId")
    List<DatasetRoleEntity> findRolesByDatasetId(@Param("datasetId") UUID datasetId);
}

