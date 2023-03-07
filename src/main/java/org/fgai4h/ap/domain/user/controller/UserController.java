package org.fgai4h.ap.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.UserApi;
import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.user.entity.AnnotatorEntity;
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
import org.fgai4h.ap.domain.user.service.UserService;
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
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserRepository userRepository;
    private final AnnotatorRepository annotatorRepository;
    private final ReviewerRepository reviewerRepository;
    private final SupervisorModelAssembler supervisorModelAssembler;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;
    private final ReviewerModelAssembler reviewerModelAssembler;
    private final SupervisorRepository supervisorRepository;

    private final UserService userService;
    private UserApiMapper userMapper;


    @GetMapping("/annotators")
    public ResponseEntity<CollectionModel<AnnotatorModel>> getAllAnnotators()
    {
        List<AnnotatorEntity> annotatorEntities = annotatorRepository.findAll();
        return new ResponseEntity<>(
                annotatorModelAssembler.toCollectionModel(annotatorEntities),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateUser(UserDto userDto) {
        UserModel userModel = userMapper.toUserModel(userDto);
        userService.updateUser(userModel);
        return ResponseEntity.noContent().build();
    }


    @Override
    public ResponseEntity<UserDto> getUserById(UUID userId) {
        return userService.getUserById(userId)
                .map(userMapper::toUserDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/api/v1/users")
    public ResponseEntity<?> addUser(@RequestBody UserEntity newUser){
        newUser = userRepository.save(newUser);

        Link newlyCreatedLink = linkTo(methodOn(UserController.class).getUserById(newUser.getUserUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to create " + newUser);
        }
    }



    @GetMapping("/annotators/{id}")
    public ResponseEntity<AnnotatorModel> getAnnotatorById(@PathVariable("id") UUID id) {
        return annotatorRepository.findById(id)
                .map(annotatorModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<ReviewerModel> getReviewerById(@PathVariable("id") UUID id) {
        return reviewerRepository.findById(id)
                .map(reviewerModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<SupervisorModel> getSupervisorById(@PathVariable("id") UUID id) {
        return supervisorRepository.findById(id)
                .map(supervisorModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
