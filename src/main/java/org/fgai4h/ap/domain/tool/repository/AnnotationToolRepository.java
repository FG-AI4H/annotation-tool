package org.fgai4h.ap.domain.tool.repository;

import org.fgai4h.ap.domain.tool.entity.AnnotationToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnotationToolRepository extends JpaRepository<AnnotationToolEntity, UUID> {

}
