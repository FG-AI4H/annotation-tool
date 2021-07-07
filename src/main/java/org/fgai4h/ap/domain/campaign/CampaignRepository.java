package org.fgai4h.ap.domain.campaign;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
}
