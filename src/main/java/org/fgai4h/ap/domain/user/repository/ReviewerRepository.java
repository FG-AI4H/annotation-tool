package org.fgai4h.ap.domain.user.repository;

import org.fgai4h.ap.domain.user.entity.ReviewerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewerRepository extends JpaRepository<ReviewerEntity, UUID> {
}
