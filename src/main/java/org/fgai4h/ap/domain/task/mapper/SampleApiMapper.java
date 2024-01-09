package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.SampleDto;
import org.fgai4h.ap.domain.task.model.SampleModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SampleApiMapper {

    @Mapping(source = "id", target = "sampleUUID")
    SampleModel toSampleModel(SampleDto annotationTaskDto);

    default UUID map(SampleModel value){
        return value.getSampleUUID();
    }

    @InheritInverseConfiguration
    SampleDto toSampleDto(final SampleModel annotationTaskModel);

}
