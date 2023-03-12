package org.fgai4h.ap.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.UserApi;
import org.fgai4h.ap.api.model.AnnotatorDto;
import org.fgai4h.ap.api.model.ReviewerDto;
import org.fgai4h.ap.api.model.SupervisorDto;
import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.user.mapper.AnnotatorApiMapper;
import org.fgai4h.ap.domain.user.mapper.ReviewerApiMapper;
import org.fgai4h.ap.domain.user.mapper.SupervisorApiMapper;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {


    private final UserService userService;
    private final UserApiMapper userApiMapper;
    private final AnnotatorApiMapper annotatorApiMapper;
    private final ReviewerApiMapper reviewerApiMapper;
    private final SupervisorApiMapper supervisorApiMapper;


    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(
                userService.getAllUsers().stream().map(userApiMapper::toUserDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateUser(UUID userId, UserDto userDto) {
        UserModel userModel = userApiMapper.toUserModel(userDto);
        userService.updateUser(userModel);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID userId) {
        return userService.getUserById(userId)
                .map(userApiMapper::toUserDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AnnotatorDto> getAnnotatorById(UUID annotatorId) {
        return userService.getAnnotatorById(annotatorId)
                .map(annotatorApiMapper::toAnnotatorDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ReviewerDto> getReviewerById(UUID reviewerId) {
        return userService.getReviewerById(reviewerId)
                .map(reviewerApiMapper::toReviewerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SupervisorDto> getSupervisorById(UUID supervisorId) {
        return userService.getSupervisorById(supervisorId)
                .map(supervisorApiMapper::toSupervisorDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
