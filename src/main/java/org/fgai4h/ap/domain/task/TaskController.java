package org.fgai4h.ap.domain.task;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Iterator;
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


    @GetMapping("/api/v1/tasks")
    public ResponseEntity<CollectionModel<TaskModel>> getAllTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return new ResponseEntity<>(
                taskModelAssembler.toCollectionModel(taskEntities),
                HttpStatus.OK);
    }

    @GetMapping("/api/v1/tasks/me")
    public ResponseEntity<CollectionModel<TaskModel>> getMyTasks(Principal principal) {
        List<TaskEntity> taskEntities = taskRepository.findMyTasks(principal.getName());
        return new ResponseEntity<>(
                taskModelAssembler.toCollectionModel(taskEntities),
                HttpStatus.OK);
    }

    @GetMapping("/api/v1/tasks/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable("id") UUID id) {
        return taskRepository.findById(id)
                .map(taskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/tasks/{id}")
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

    @PutMapping("/api/v1/tasks/{id}/next")
    public ResponseEntity<?> updateTaskNext(@RequestBody TaskEntity task, @PathVariable UUID id){
        TaskEntity taskToUpdate = task;
        taskToUpdate.setSamples(null);

        for (Iterator<AnnotationEntity> it = task.getAnnotations().iterator(); it.hasNext(); ) {
            AnnotationEntity annotationEntity = it.next();
            annotationEntity.setAnnotationTask(null);
            annotationEntity.setTask(task);

            for (Iterator<AnnotationDataEntity> itd = annotationEntity.getAnnotationDataList().iterator(); itd.hasNext(); ) {
                AnnotationDataEntity annotationDataEntity = itd.next();
                annotationDataEntity.setAnnotationEntity(annotationEntity);
            }
        }

        taskToUpdate.setTaskUUID(id);
        taskRepository.save(taskToUpdate);

        UUID nextTaskId = UUID.fromString("b4009387-4d48-49b6-be2a-8ad50df03307");
        Link newlyCreatedLink = linkTo(methodOn(TaskController.class).getTaskById(nextTaskId)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + taskToUpdate);
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

    @GetMapping("/api/v1/annotationtasks/{id}")
    public ResponseEntity<AnnotationTaskModel> getAnnotationTaskById(@PathVariable("id") UUID id) {
        return annotationTaskRepository.findById(id)
                .map(annotationTaskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
