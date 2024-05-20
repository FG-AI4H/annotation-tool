package org.fgai4h.ap.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "qualification")
public class QualificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    
    private UUID qualificationUUID;

    private String degree;
    private String expertise;
    private String specialization;
    private String country;

    @ManyToOne
    @JoinColumn(name = "annotator_entity_user_uuid")
    private AnnotatorEntity annotatorEntity;

}
