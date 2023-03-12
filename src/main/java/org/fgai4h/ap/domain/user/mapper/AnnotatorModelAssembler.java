package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.AnnotatorEntity;
import org.fgai4h.ap.domain.user.model.AnnotatorModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotatorModelAssembler extends RepresentationModelAssemblerSupport<AnnotatorEntity, AnnotatorModel> {

    public AnnotatorModelAssembler() {
        super(UserController.class, AnnotatorModel.class);
    }

    @Override
    public AnnotatorModel toModel(AnnotatorEntity entity) {
        if(isNull(entity)){
            return null;
        }

        AnnotatorModel annotatorModel = instantiateModel(entity);

        annotatorModel.add(linkTo(
                methodOn(UserController.class)
                        .getAnnotatorById(entity.getAnnotatorUUID()))
                .withSelfRel());

        annotatorModel.setAnnotatorUUID(entity.getAnnotatorUUID());
        annotatorModel.setExpertise(entity.getExpertise());
        annotatorModel.setExpectedSalary(entity.getExpectedSalary());
        annotatorModel.setSelfAssessment(entity.getSelfAssessment());
        annotatorModel.setStudyCountry(entity.getStudyCountry());
        annotatorModel.setWorkCountry(entity.getWorkCountry());
        annotatorModel.setYearsInPractice(entity.getYearsInPractice());
        annotatorModel.setDegree(entity.getDegree());

        return annotatorModel;
    }

    public AnnotatorEntity toEntity(AnnotatorModel annotatorRole) {

        AnnotatorEntity annotatorEntity = new AnnotatorEntity();

        annotatorEntity.setAnnotatorUUID(annotatorRole.getAnnotatorUUID());
        annotatorEntity.setExpertise(annotatorRole.getExpertise());
        annotatorEntity.setExpectedSalary(annotatorRole.getExpectedSalary());
        annotatorEntity.setSelfAssessment(annotatorRole.getSelfAssessment());
        annotatorEntity.setStudyCountry(annotatorRole.getStudyCountry());
        annotatorEntity.setWorkCountry(annotatorRole.getWorkCountry());
        annotatorEntity.setYearsInPractice(annotatorRole.getYearsInPractice());
        annotatorEntity.setDegree(annotatorRole.getDegree());

        return annotatorEntity;
    }

    public List<AnnotatorModel> toAnnotatorModel(List<AnnotatorEntity> annotators) {
        if (annotators.isEmpty())
            return Collections.emptyList();

        return annotators.stream()
                .map(annotator-> AnnotatorModel.builder()
                        .annotatorUUID(annotator.getAnnotatorUUID())
                        .degree(annotator.getDegree())
                        .expertise(annotator.getExpertise())
                        .expectedSalary(annotator.getExpectedSalary())
                        .selfAssessment(annotator.getSelfAssessment())
                        .studyCountry(annotator.getStudyCountry())
                        .workCountry(annotator.getWorkCountry())
                        .yearsInPractice(annotator.getYearsInPractice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CollectionModel<AnnotatorModel> toCollectionModel(Iterable<? extends AnnotatorEntity> entities)
    {
        CollectionModel<AnnotatorModel> annotatorModels = super.toCollectionModel(entities);

        return annotatorModels;
    }
}
