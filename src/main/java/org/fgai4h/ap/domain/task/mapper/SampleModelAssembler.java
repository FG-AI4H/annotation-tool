package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.domain.task.controller.TaskController;
import org.fgai4h.ap.domain.task.entity.SampleEntity;
import org.fgai4h.ap.domain.task.model.SampleModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SampleModelAssembler extends RepresentationModelAssemblerSupport<SampleEntity, SampleModel> {

    public SampleModelAssembler() {
        super(TaskController.class, SampleModel.class);
    }

    @Override
    public @NotNull SampleModel toModel(@NotNull SampleEntity entity) {
        SampleModel sampleModel = instantiateModel(entity);

        sampleModel.add(linkTo(
                methodOn(TaskController.class)
                        .getSampleById(entity.getSampleUUID()))
                .withSelfRel());

        sampleModel.setSampleUUID(entity.getSampleUUID());
        sampleModel.setData(entity.getData());
        sampleModel.setTitle(entity.getTitle());

        return sampleModel;
    }

}
