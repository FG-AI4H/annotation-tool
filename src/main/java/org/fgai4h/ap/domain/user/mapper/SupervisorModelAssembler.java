package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.SupervisorEntity;
import org.fgai4h.ap.domain.user.model.SupervisorModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SupervisorModelAssembler extends RepresentationModelAssemblerSupport<SupervisorEntity, SupervisorModel> {

    public SupervisorModelAssembler() {
        super(UserController.class, SupervisorModel.class);
    }

    @Override
    public SupervisorModel toModel(SupervisorEntity entity) {
        if(isNull(entity)){
            return null;
        }

        SupervisorModel supervisorModel = instantiateModel(entity);

        supervisorModel.add(linkTo(
                methodOn(UserController.class)
                        .getSupervisorById(entity.getSupervisorUUID()))
                .withSelfRel());

        supervisorModel.setSupervisorUUID(entity.getSupervisorUUID());
        supervisorModel.setExpectedSalary(entity.getExpectedSalary());
        supervisorModel.setSelfAssessment(entity.getSelfAssessment());


        return supervisorModel;
    }

}
