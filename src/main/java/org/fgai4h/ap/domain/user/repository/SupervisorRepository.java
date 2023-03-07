package org.fgai4h.ap.domain.user.repository;

import org.fgai4h.ap.domain.user.entity.SupervisorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupervisorRepository extends JpaRepository<SupervisorEntity, UUID> {
}
