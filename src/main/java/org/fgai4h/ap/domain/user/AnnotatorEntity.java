package org.fgai4h.ap.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AnnotatorEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID annotatorUUID;

    private String expertise;
    private Integer yearsInPractice;
    private Float expectedSalary;
    private String workCountry;
    private String studyCountry;
    private Integer selfAssessment;
    private String degree;

    @OneToMany(mappedBy = "annotatorEntity")
    private List<QualificationEntity> qualifications = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<SkillEntity> clinicalSkills = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<AvailabilityEntity> availabilities = new java.util.ArrayList<>();


}
