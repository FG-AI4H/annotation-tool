package org.fgai4h.ap.domain.task;

import org.fgai4h.ap.domain.campaign.CampaignEntity;
import org.fgai4h.ap.domain.campaign.CampaignModel;
import org.fgai4h.ap.domain.campaign.CampaignModelAssembler;
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
    private final TaskModelAssembler taskModelAssembler;

    public CampaignController(TaskRepository taskRepository, TaskModelAssembler taskModelAssembler) {
        this.taskRepository = taskRepository;
        this.taskModelAssembler = taskModelAssembler;
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
}
