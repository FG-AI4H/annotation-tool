package org.fgai4h.ap.domain.campaign.repository;

import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
}
