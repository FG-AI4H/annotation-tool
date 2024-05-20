package org.fgai4h.ap.domain.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="task")
public class TaskEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    
    private UUID taskUUID;

    private String kind;
    private Boolean readOnly;

    @ManyToMany
    @JoinTable(
            name = "task_annotation",
            joinColumns = @JoinColumn(name = "taskUUID"),
            inverseJoinColumns = @JoinColumn(name = "annotationTaskUUID"))
    private List<AnnotationTaskEntity> annotationTasks;

    @OneToMany(mappedBy="task", cascade=CascadeType.ALL)
    private List<SampleEntity> samples;

    @OneToMany(mappedBy="task", cascade=CascadeType.ALL)
    private List<AnnotationEntity> annotations;

    @ManyToOne
    @JoinColumn(name = "assignee_user_uuid")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "campaign_campaign_uuid")
    private CampaignEntity campaign;


}
