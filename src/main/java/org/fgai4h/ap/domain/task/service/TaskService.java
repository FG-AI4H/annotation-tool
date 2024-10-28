package org.fgai4h.ap.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.task.mapper.*;
import org.fgai4h.ap.domain.task.model.*;
import org.fgai4h.ap.domain.task.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskModelAssembler taskModelAssembler;
    private final AnnotationDataRepository annotationDataRepository;
    private final AnnotationDataModelAssembler annotationDataModelAssembler;
    private final SampleRepository sampleRepository;
    private final SampleModelAssembler sampleModelAssembler;
    private final AnnotationTaskRepository annotationTaskRepository;
    private final AnnotationTaskModelAssembler annotationTaskModelAssembler;
    private final AnnotationRepository annotationRepository;
    private final AnnotationModelAssembler annotationModelAssembler;

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskModelAssembler::toModel).collect(Collectors.toList());
    }

    public AnnotationDataModel getAnnotationDataById(UUID id){
        return annotationDataRepository
                .findById(id)
                .map(annotationDataModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Annotation Data", "id", id));
    }

    public Optional<AnnotationTaskModel> getAnnotationTaskById(UUID id){
        return Optional.ofNullable(annotationTaskRepository
                .findById(id)
                .map(annotationTaskModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Annotation Task", "id", id)));
    }

    public List<TaskModel> getTasksByCampaignId(UUID campaignId) {
        return taskRepository.findByCampaignId(campaignId).stream()
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public AnnotationTaskModel findAnnotationTaskById(UUID id){
        return getAnnotationTaskById(id).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Annotation Task", "id", id));
    }

    public SampleModel getSampleById(UUID id){
        return sampleRepository
                .findById(id)
                .map(sampleModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Sample", "id", id));
    }

    public AnnotationModel getAnnotationById(UUID id){
        return annotationRepository
                .findById(id)
                .map(annotationModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Annotation", "id", id));
    }

    public Optional<TaskModel> getTaskById(UUID taskId) {
        return Optional.ofNullable(taskRepository
                .findById(taskId)
                .map(taskModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Task", "id", taskId)));
    }

    public TaskModel findById(UUID taskId){
        return getTaskById(taskId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Task", "id", taskId));
    }

    public void deleteTaskById(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<TaskModel> getMyTasks(String userIdpId) {
        return taskRepository.findMyTasks(userIdpId).stream()
                .map(taskModelAssembler::toModel).collect(Collectors.toList());
    }
}
