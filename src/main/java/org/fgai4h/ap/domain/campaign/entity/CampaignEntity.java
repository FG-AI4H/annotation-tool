package org.fgai4h.ap.domain.campaign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID campaignUUID;

    private String name;
    private String description;
    private String status;
    private String annotationKind;
    private String annotationTool;
    private String preAnnotationTool;
    private String preAnnotationModel;
    private String annotationMethod;
    private String annotationInstructions;
    private String qualityAssurance;
    private Boolean isInstanceLabel;
    private Integer minAnnotation;

    @OneToMany(mappedBy = "campaignEntity")
    private List<ClassLabelEntity> classLabels = new java.util.ArrayList<>();

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name = "campaign_annotator",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> annotators;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name = "campaign_reviewer",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> reviewers;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name = "campaign_supervisor",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> supervisors;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name = "campaign_dataset",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "datasetUUID"))
    private List<DatasetEntity> datasets;

    public CampaignEntity(UUID uuid, String name, String description) {
        this.campaignUUID = uuid;
        this.name = name;
        this.description = description;
    }
}
