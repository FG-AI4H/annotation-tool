package org.fgai4h.ap.domain.catalog.repository;

import org.fgai4h.ap.domain.catalog.entity.DataCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DataCatalogRepository extends JpaRepository<DataCatalogEntity, UUID> {

}

