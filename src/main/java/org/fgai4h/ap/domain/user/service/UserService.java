package org.fgai4h.ap.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.mapper.*;
import org.fgai4h.ap.domain.user.model.AnnotatorModel;
import org.fgai4h.ap.domain.user.model.ReviewerModel;
import org.fgai4h.ap.domain.user.model.SupervisorModel;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.repository.AnnotatorRepository;
import org.fgai4h.ap.domain.user.repository.ReviewerRepository;
import org.fgai4h.ap.domain.user.repository.SupervisorRepository;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AnnotatorRepository annotatorRepository;
    private final ReviewerRepository reviewerRepository;
    private final SupervisorRepository supervisorRepository;
    private final UserModelAssembler userModelAssembler;
    private final SupervisorModelAssembler supervisorModelAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;
    private final ReviewerModelAssembler reviewerModelAssembler;
    private final UserMapper userMapper;


    public Optional<UserModel> getUserById(final UUID userId) {
        return Optional.ofNullable(userRepository
                .findById(userId)
                .map(userModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "User", "id", userId)));

    }

    public UserModel findById(final UUID userId) {
        return getUserById(userId).get();

    }

    public Optional<UserModel> getUserByIdpId(final String idpId) {
        return Optional.ofNullable(userRepository
                .findByIdpId(idpId)
                .map(userModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "User", "id", idpId)));
    }

    public void updateUser(UserModel userModel){
        UserEntity userEntity = userMapper.toUserEntity(userModel);
        userRepository.save(userEntity);

    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userModelAssembler::toModel).collect(Collectors.toList());
    }

    public List<UserModel> getUsers(List<UUID> idList){
        List<UserModel> list = new ArrayList<UserModel>();
        if(idList != null) {
            list = idList.stream().map(id -> getUserById(id).get()).collect(Collectors.toList());
        }

        return list;
    }

    public Optional<AnnotatorModel> getAnnotatorById(UUID annotatorId) {
        return Optional.ofNullable(annotatorRepository
                .findById(annotatorId)
                .map(annotatorModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Annotator", "id", annotatorId)));
    }

    public AnnotatorModel findAnnotatorById(final UUID userId) {
        return getAnnotatorById(userId).get();

    }

    public Optional<ReviewerModel> getReviewerById(UUID reviewerId) {
        return Optional.ofNullable(reviewerRepository
                .findById(reviewerId)
                .map(reviewerModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Reviewer", "id", reviewerId)));
    }

    public ReviewerModel findReviewerById(final UUID userId) {
        return getReviewerById(userId).get();

    }

    public Optional<SupervisorModel> getSupervisorById(UUID supervisorId) {
        return Optional.ofNullable(supervisorRepository
                .findById(supervisorId)
                .map(supervisorModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Supervisor", "id", supervisorId)));
    }

    public SupervisorModel findSupervisorById(final UUID userId) {
        return getSupervisorById(userId).get();

    }
}
