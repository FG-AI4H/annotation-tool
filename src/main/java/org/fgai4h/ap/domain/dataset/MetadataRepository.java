package org.fgai4h.ap.domain.dataset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface MetadataRepository extends JpaRepository<MetadataEntity, UUID> {
}
