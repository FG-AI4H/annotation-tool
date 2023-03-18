package org.fgai4h.ap.domain.task.repository;

import org.fgai4h.ap.domain.task.entity.AnnotationDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnotationDataRepository extends JpaRepository<AnnotationDataEntity, UUID> {

}

