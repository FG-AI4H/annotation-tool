package org.fgai4h.ap.domain.task;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
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
