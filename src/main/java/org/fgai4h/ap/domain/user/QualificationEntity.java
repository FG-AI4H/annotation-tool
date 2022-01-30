package org.fgai4h.ap.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "qualification")
public class QualificationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID qualificationUUID;

    private String degree;
    private String expertise;
    private String specialization;
    private String country;

    @ManyToOne
    @JoinColumn(name = "annotator_entity_user_uuid")
    private AnnotatorEntity annotatorEntity;

}
