package org.fgai4h.ap.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnotatorRepository extends JpaRepository<AnnotatorEntity, UUID> {
}
