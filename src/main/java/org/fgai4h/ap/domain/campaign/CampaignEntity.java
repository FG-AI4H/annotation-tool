package org.fgai4h.ap.domain.campaign;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="campaign")
public class CampaignEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID campaignUUID;

    public String name;
    public String description;

}
