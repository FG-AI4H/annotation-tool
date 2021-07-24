package org.fgai4h.ap.domain.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID taskUUID;

    private String kind;
    private Boolean readOnly;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "task_annotation",
            joinColumns = @JoinColumn(name = "taskUUID"),
            inverseJoinColumns = @JoinColumn(name = "annotationTaskUUID"))
    private List<AnnotationTaskEntity> annotationTasks;

    @OneToMany(mappedBy="task")
    @OrderBy("name ASC")
    private List<SampleEntity> samples;

    @OneToMany(mappedBy="task")
    @OrderBy("name ASC")
    private List<AnnotationEntity> annotations;


}
