package org.fgai4h.ap.domain.catalog.repository;

import org.fgai4h.ap.domain.catalog.entity.DataAccessRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DataAccessRequestRepository extends JpaRepository<DataAccessRequestEntity, UUID> {

    @Query("SELECT e FROM DataAccessRequestEntity e WHERE e.dataOwner.idpId = :userId")
    List<DataAccessRequestEntity> findAllByOwnerId(@Param("userId") String userId);

    @Query("SELECT e FROM DataAccessRequestEntity e WHERE e.requester.idpId = :userId")
    List<DataAccessRequestEntity> findAllByRequesterId(@Param("userId") String userId);

    @Query("SELECT e FROM DataAccessRequestEntity e WHERE e.dataset.id = :datasetId")
    DataAccessRequestEntity findByDatasetId(@Param("datasetId") UUID datasetId);
}
