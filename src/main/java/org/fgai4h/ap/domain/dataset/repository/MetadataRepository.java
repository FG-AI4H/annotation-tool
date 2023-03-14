package org.fgai4h.ap.domain.dataset.repository;

import org.fgai4h.ap.domain.dataset.entity.DatasetMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetadataRepository extends JpaRepository<DatasetMetadataEntity, UUID> {
}
