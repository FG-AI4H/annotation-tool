package org.fgai4h.ap.domain.task.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.TaskApi;
import org.fgai4h.ap.api.model.AnnotationDataDto;
import org.fgai4h.ap.api.model.AnnotationTaskDto;
import org.fgai4h.ap.api.model.TaskDto;
import org.fgai4h.ap.domain.task.entity.AnnotationDataEntity;
import org.fgai4h.ap.domain.task.entity.AnnotationEntity;
import org.fgai4h.ap.domain.task.entity.AnnotationTaskEntity;
import org.fgai4h.ap.domain.task.entity.TaskEntity;
import org.fgai4h.ap.domain.task.mapper.*;
import org.fgai4h.ap.domain.task.model.AnnotationModel;
import org.fgai4h.ap.domain.task.model.AnnotationTaskModel;
import org.fgai4h.ap.domain.task.model.SampleModel;
import org.fgai4h.ap.domain.task.model.TaskModel;
import org.fgai4h.ap.domain.task.repository.AnnotationRepository;
import org.fgai4h.ap.domain.task.repository.AnnotationTaskRepository;
import org.fgai4h.ap.domain.task.repository.SampleRepository;
import org.fgai4h.ap.domain.task.repository.TaskRepository;
import org.fgai4h.ap.domain.task.service.TaskService;
import org.fgai4h.ap.helpers.AWSS3;
import org.fgai4h.ap.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final TaskRepository taskRepository;
    private final AnnotationRepository annotationRepository;
    private final SampleRepository sampleRepository;
    private final AnnotationTaskRepository annotationTaskRepository;

    private final TaskModelAssembler taskModelAssembler;
    private final AnnotationModelAssembler annotationModelAssembler;
    private final SampleModelAssembler sampleModelAssembler;
    private final AnnotationTaskModelAssembler annotationTaskModelAssembler;
    private final TaskService taskService;
    private final TaskApiMapper taskApiMapper;
    private final AnnotationTaskApiMapper annotationTaskApiMapper;


    @Override
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return new ResponseEntity<>(
                taskService.getAllTasks().stream().map(taskApiMapper::toTaskDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskDto> getTaskById(UUID taskId) {
        return taskService.getTaskById(taskId)
                .map(taskApiMapper::toTaskDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<AnnotationDataDto>> getAnnotationDataById(UUID annotationDataId) {
        return TaskApi.super.getAnnotationDataById(annotationDataId);
    }
    @Override
    public ResponseEntity<List<TaskDto>> getTasksByCampaignId(UUID campaignId) {
        return new ResponseEntity<>(
                taskService.getTasksByCampaignId(campaignId).stream().map(taskApiMapper::toTaskDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AnnotationTaskDto> getAnnotationTaskById(UUID annotationTaskId) {
        return taskService.getAnnotationTaskById(annotationTaskId)
                .map(annotationTaskApiMapper::toAnnotationTaskDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteTaskById(UUID taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TaskDto>> getMyTasks() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return new ResponseEntity<>(
                taskService.getMyTasks(authentication.getName()).stream().map(taskApiMapper::toTaskDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }


    @PutMapping("/api/v1/tasks/{id}")
    public ResponseEntity<?> updateTask(@RequestBody TaskEntity task, @PathVariable UUID id){
        task.setTaskUUID(id);
        taskRepository.save(task);

        Link newlyCreatedLink = linkTo(methodOn(TaskController.class).getTaskById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + task);
        }
    }

    @PutMapping("/api/v1/tasks/{id}/next")
    public ResponseEntity<?> updateTaskNext(@RequestBody TaskEntity task, @PathVariable UUID id){
        task.setSamples(null);

        for (AnnotationEntity annotationEntity : task.getAnnotations()) {
            annotationEntity.setAnnotationTask(null);
            annotationEntity.setTask(task);

            for (AnnotationDataEntity annotationDataEntity : annotationEntity.getAnnotationDataList()) {
                annotationDataEntity.setAnnotationEntity(annotationEntity);

                // extract the annotation data and save it as a file in the S3 bucket
                String annotationData = annotationDataEntity.getData(); // assuming the data is a String
                byte[] annotationDataBytes = annotationData.getBytes(StandardCharsets.UTF_8);

                // assuming you have a DataCatalogModel instance named dataCatalogModel
                // and fileType and keyName are defined

                //AWSS3.putObject(dataCatalogModel, fileType, keyName, annotationDataBytes);

            }
        }

        task.setTaskUUID(id);
        taskRepository.save(task);

        // TODO: This is a dummy UUID, replace it with the actual next task UUID
        UUID nextTaskId = UUID.fromString("b4009387-4d48-49b6-be2a-8ad50df03307");
        Link newlyCreatedLink = linkTo(methodOn(TaskController.class).getTaskById(nextTaskId)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + task);
        }
    }

    @GetMapping("/api/v1/annotations")
    public ResponseEntity<CollectionModel<AnnotationModel>> getAllAnnotations() {
        List<AnnotationEntity> annotationEntities = annotationRepository.findAll();
        return new ResponseEntity<>(
                annotationModelAssembler.toCollectionModel(annotationEntities),
                HttpStatus.OK);
    }


    @GetMapping("/api/v1/annotations/{id}")
    public ResponseEntity<AnnotationModel> getAnnotationById(@PathVariable("id") UUID id) {
        return annotationRepository.findById(id)
                .map(annotationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/v1/annotations/{id}")
    public void deleteAnnotationById(@PathVariable("id") UUID id) {
        annotationRepository.deleteById(id);
    }

    @GetMapping("/api/v1/samples/{id}")
    public ResponseEntity<SampleModel> getSampleById(@PathVariable("id") UUID id) {
        return sampleRepository.findById(id)
                .map(sampleModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/v1/annotationtasks")
    public ResponseEntity<CollectionModel<AnnotationTaskModel>> getAllAnnotationTask() {
        List<AnnotationTaskEntity> annotationTaskEntities = annotationTaskRepository.findAll();
        return new ResponseEntity<>(
                annotationTaskModelAssembler.toCollectionModel(annotationTaskEntities),
                HttpStatus.OK);
    }
}
