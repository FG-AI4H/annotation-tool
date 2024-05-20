package org.fgai4h.ap.domain.tool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.task.entity.AnnotationTaskEntity;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="annotationTool")
public class AnnotationToolEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    
    private UUID annotationToolUUID;

    private String name;
    private String description;
    private String editor;

    @ManyToMany
    @JoinTable(
            name = "tool_task_annotation",
            joinColumns = @JoinColumn(name = "annotationToolUUID"),
            inverseJoinColumns = @JoinColumn(name = "annotationTaskUUID"))
    private List<AnnotationTaskEntity> annotationTasks;
}
