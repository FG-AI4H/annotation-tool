package org.fgai4h.ap.domain.campaign;

import java.util.UUID;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(UUID id) {
        super("Could not find campaign " + id);
    }
}
