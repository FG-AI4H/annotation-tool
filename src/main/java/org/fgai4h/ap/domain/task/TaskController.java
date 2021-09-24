package org.fgai4h.ap.domain.task;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TaskController {

    private final TaskRepository taskRepository;
    private final AnnotationRepository annotationRepository;
    private final SampleRepository sampleRepository;
    private final AnnotationTaskRepository annotationTaskRepository;

    private final TaskModelAssembler taskModelAssembler;
    private final AnnotationModelAssembler annotationModelAssembler;
    private final SampleModelAssembler sampleModelAssembler;
    private final AnnotationTaskModelAssembler annotationTaskModelAssembler;
    ;

    public TaskController(TaskRepository taskRepository, TaskModelAssembler taskModelAssembler,
                          AnnotationRepository annotationRepository, AnnotationModelAssembler annotationModelAssembler,
                          SampleRepository sampleRepository, SampleModelAssembler sampleModelAssembler,
                          AnnotationTaskRepository annotationTaskRepository, AnnotationTaskModelAssembler annotationTaskModelAssembler) {
        this.taskRepository = taskRepository;
        this.taskModelAssembler = taskModelAssembler;
        this.annotationRepository = annotationRepository;
        this.annotationModelAssembler = annotationModelAssembler;
        this.sampleRepository = sampleRepository;
        this.sampleModelAssembler = sampleModelAssembler;
        this.annotationTaskRepository = annotationTaskRepository;
        this.annotationTaskModelAssembler = annotationTaskModelAssembler;
    }


    @GetMapping("/tasks")
    public ResponseEntity<CollectionModel<TaskModel>> getAllTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return new ResponseEntity<>(
                taskModelAssembler.toCollectionModel(taskEntities),
                HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable("id") UUID id) {
        return taskRepository.findById(id)
                .map(taskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(@RequestBody TaskEntity task, @PathVariable UUID id){
        TaskEntity taskToUpdate = task;
        taskToUpdate.setTaskUUID(id);
        taskRepository.save(taskToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(TaskController.class).getTaskById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + taskToUpdate);
        }
    }

    @GetMapping("/annotations")
    public ResponseEntity<CollectionModel<AnnotationModel>> getAllAnnotations() {
        List<AnnotationEntity> annotationEntities = annotationRepository.findAll();
        return new ResponseEntity<>(
                annotationModelAssembler.toCollectionModel(annotationEntities),
                HttpStatus.OK);
    }


    @GetMapping("/annotations/{id}")
    public ResponseEntity<AnnotationModel> getAnnotationById(@PathVariable("id") UUID id) {
        return annotationRepository.findById(id)
                .map(annotationModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/samples/{id}")
    public ResponseEntity<SampleModel> getSampleById(@PathVariable("id") UUID id) {
        return sampleRepository.findById(id)
                .map(sampleModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/annotationtasks")
    public ResponseEntity<CollectionModel<AnnotationTaskModel>> getAllAnnotationTask() {
        List<AnnotationTaskEntity> annotationTaskEntities = annotationTaskRepository.findAll();
        return new ResponseEntity<>(
                annotationTaskModelAssembler.toCollectionModel(annotationTaskEntities),
                HttpStatus.OK);
    }

    @GetMapping("/annotationtasks/{id}")
    public ResponseEntity<AnnotationTaskModel> getAnnotationTaskById(@PathVariable("id") UUID id) {
        return annotationTaskRepository.findById(id)
                .map(annotationTaskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
