package org.fgai4h.ap.domain.tool.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.tool.entity.AnnotationToolEntity;
import org.fgai4h.ap.domain.tool.mapper.AnnotationToolMapper;
import org.fgai4h.ap.domain.tool.mapper.AnnotationToolModelAssembler;
import org.fgai4h.ap.domain.tool.model.AnnotationToolModel;
import org.fgai4h.ap.domain.tool.repository.AnnotationToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnotationToolService {

    private final AnnotationToolRepository annotationToolRepository;
    private final AnnotationToolModelAssembler annotationToolModelAssembler;
    private final AnnotationToolMapper annotationToolMapper;

    public List<AnnotationToolModel> getAnotationTools() {
        return annotationToolRepository.findAll().stream()
                .map(annotationToolModelAssembler::toModel).collect(Collectors.toList());
    }

    public AnnotationToolModel addAnnotationTool(AnnotationToolModel annotationToolModel) {
        AnnotationToolEntity newAnnotationTool = annotationToolRepository.save(annotationToolMapper.toAnnotationToolEntity(annotationToolModel));
        return annotationToolModelAssembler.toModel(newAnnotationTool);
    }
}
