package org.fgai4h.ap.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SampleRepository extends JpaRepository<SampleEntity, UUID> {
}

