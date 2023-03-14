package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.SupervisorEntity;
import org.fgai4h.ap.domain.user.model.SupervisorModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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


        return supervisorModel;
    }

    public List<SupervisorModel> toSupervisorModel(List<SupervisorEntity> supervisors) {
        if (supervisors.isEmpty())
            return Collections.emptyList();

        return supervisors.stream()
                .map(supervisor-> SupervisorModel.builder()
                        .supervisorUUID(supervisor.getSupervisorUUID())
                        .build())
                .collect(Collectors.toList());
    }

    public SupervisorEntity toEntity(SupervisorModel supervisorModel) {
        SupervisorEntity entity = new SupervisorEntity();
        entity.setSupervisorUUID(supervisorModel.getSupervisorUUID());

        return entity;
    }
}
