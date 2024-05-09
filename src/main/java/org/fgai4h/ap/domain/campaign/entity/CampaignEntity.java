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

/**
 * Represents a campaign in the system.
 * A campaign is a set of tasks that are to be annotated by a group of annotators.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "campaign")
public class CampaignEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the campaign.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID campaignUUID;

    /**
     * The name for the campaign.
     */
    private String name;
    /**
     * The description for the campaign.
     */
    private String description;
    /**
     * The status of the campaign.
     */
    private String status;
    /**
     * The kind of annotation that is to be performed in the campaign.
     */
    private String annotationKind;
    /**
     * The tool that is to be used for annotation.
     */
    private String annotationTool;
    /**
     * The model that is to be used for annotation.
     */
    private String preAnnotationTool;
    /**
     * The model that is to be used for annotation.
     */
    private String preAnnotationModel;
    /**
     * The method that is to be used for annotation.
     */
    private String annotationMethod;
    /**
     * The instructions for annotation.
     */
    private String annotationInstructions;
    /**
     * The quality assurance for annotation.
     */
    private String qualityAssurance;
    /**
     * The kind of quality assurance that is to be performed in the campaign.
     */
    private Boolean isInstanceLabel;
    /**
     * The kind of quality assurance that is to be performed in the campaign.
     */
    private Integer minAnnotation;

    /**
     * The class labels for the campaign.
     */
    @OneToMany(mappedBy = "campaignEntity")
    private List<ClassLabelEntity> classLabels = new java.util.ArrayList<>();

    /**
     * The annotators for the campaign.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "campaign_annotator",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> annotators;

    /**
     * The reviewers for the campaign.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "campaign_reviewer",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> reviewers;

    /**
     * The supervisors for the campaign.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "campaign_supervisor",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "userUUID"))
    private List<UserEntity> supervisors;

    /**
     * The datasets for the campaign.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "campaign_dataset",
            joinColumns = @JoinColumn(name = "campaignUUID"),
            inverseJoinColumns = @JoinColumn(name = "datasetUUID"))
    private List<DatasetEntity> datasets;

    /**
     * The constructor for a campaign.
     */
    public CampaignEntity(UUID uuid, String name, String description) {
        this.campaignUUID = uuid;
        this.name = name;
        this.description = description;
    }
}
