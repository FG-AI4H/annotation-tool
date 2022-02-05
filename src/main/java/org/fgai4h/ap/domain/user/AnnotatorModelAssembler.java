package org.fgai4h.ap.domain.user;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

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
}
