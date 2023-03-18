package org.fgai4h.ap.domain.task.repository;

import org.fgai4h.ap.domain.task.entity.AnnotationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnotationRepository extends JpaRepository<AnnotationEntity, UUID> {
}
