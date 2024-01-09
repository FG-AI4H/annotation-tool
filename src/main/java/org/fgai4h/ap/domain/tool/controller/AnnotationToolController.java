package org.fgai4h.ap.domain.tool.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.ToolApi;
import org.fgai4h.ap.api.model.AnnotationToolDto;
import org.fgai4h.ap.domain.tool.mapper.AnnotationToolApiMapper;
import org.fgai4h.ap.domain.tool.model.AnnotationToolModel;
import org.fgai4h.ap.domain.tool.service.AnnotationToolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AnnotationToolController implements ToolApi {

    private final AnnotationToolService annotationToolService;
    private final AnnotationToolApiMapper annotationToolApiMapper;

    @Override
    public ResponseEntity<List<AnnotationToolDto>> getAnotationTools() {
        return new ResponseEntity<>(
                annotationToolService.getAnotationTools().stream().map(annotationToolApiMapper::toAnnotationToolDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AnnotationToolDto> getAnnotationToolById(UUID annotationToolId) {
        return ToolApi.super.getAnnotationToolById(annotationToolId);
    }

    @Override
    public ResponseEntity<Void> addAnnotationTool(AnnotationToolDto annotationToolDto) {
        AnnotationToolModel annotationToolModel = annotationToolApiMapper.toAnnotationToolModel(annotationToolDto);
        annotationToolModel = annotationToolService.addAnnotationTool(annotationToolModel);

        try {
            return ResponseEntity.created(new URI(annotationToolModel.getAnnotationToolUUID().toString())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
