package org.fgai4h.ap.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AnnotatorEntity extends UserEntity implements Serializable {

    private String expertise;
    private Integer yearsInPractice;
    private Float expectedSalary;
    private String workCountry;
    private Integer selfAssessment;

    @OneToMany(mappedBy = "annotatorEntity")
    private List<QualificationEntity> qualifications = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<SkillEntity> clinicalSkills = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "annotatorEntity")
    private List<AvailabilityEntity> availabilities = new java.util.ArrayList<>();


}
