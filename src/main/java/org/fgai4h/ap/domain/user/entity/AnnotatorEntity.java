package org.fgai4h.ap.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AnnotatorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID annotatorUUID;

    private Float expectedSalary;
    private Integer selfAssessment;

    @OneToMany(mappedBy = "annotatorEntity")
    private List<QualificationEntity> qualifications = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<SkillEntity> clinicalSkills = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<AvailabilityEntity> availabilities = new java.util.ArrayList<>();


}
