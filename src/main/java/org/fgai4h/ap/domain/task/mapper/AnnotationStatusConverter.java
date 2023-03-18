package org.fgai4h.ap.domain.task.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fgai4h.ap.domain.task.model.AnnotationStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class AnnotationStatusConverter implements AttributeConverter<AnnotationStatus, String> {

    @Override
    public String convertToDatabaseColumn(AnnotationStatus annotationStatus) {
        if (annotationStatus == null) {
            return null;
        }
        return annotationStatus.getCode();
    }

    @Override
    public AnnotationStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(AnnotationStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
