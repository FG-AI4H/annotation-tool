package org.fgai4h.ap.domain.task;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
                          AnnotationRepository annotationRepository,AnnotationModelAssembler annotationModelAssembler,
                          SampleRepository sampleRepository,SampleModelAssembler sampleModelAssembler,
                          AnnotationTaskRepository annotationTaskRepository,AnnotationTaskModelAssembler annotationTaskModelAssembler) {
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
    public ResponseEntity<CollectionModel<TaskModel>> getAllTasks()
    {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return new ResponseEntity<>(
                taskModelAssembler.toCollectionModel(taskEntities),
                HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable("id") UUID id)
    {
        return taskRepository.findById(id)
                .map(taskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping("/annotationtasks/{id}")
    public ResponseEntity<AnnotationTaskModel> getAnnotationTaskById(@PathVariable("id") UUID id) {
        return annotationTaskRepository.findById(id)
                .map(annotationTaskModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
