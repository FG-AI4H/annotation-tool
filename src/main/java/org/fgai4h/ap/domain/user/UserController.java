package org.fgai4h.ap.domain.user;

import org.fgai4h.ap.api.UserApi;
import org.fgai4h.ap.helpers.AWSCognito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
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

    public UserController(UserRepository userRepository, UserModelAssembler userModelAssembler, AnnotatorRepository annotatorRepository, AnnotatorModelAssembler annotatorModelAssembler,UserModelAWSAssembler userModelAWSAssembler, ReviewerModelAssembler reviewerModelAssembler, ReviewerRepository reviewerRepository, SupervisorModelAssembler supervisorModelAssembler, SupervisorRepository supervisorRepository){
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
        this.annotatorRepository = annotatorRepository;
        this.annotatorModelAssembler = annotatorModelAssembler;
        this.userModelAWSAssembler = userModelAWSAssembler;
        this.reviewerModelAssembler = reviewerModelAssembler;
        this.reviewerRepository = reviewerRepository;
        this.supervisorModelAssembler = supervisorModelAssembler;
        this.supervisorRepository = supervisorRepository;
    }



    @GetMapping("/annotators")
    public ResponseEntity<CollectionModel<AnnotatorModel>> getAllAnnotators()
    {
        List<AnnotatorEntity> annotatorEntities = annotatorRepository.findAll();
        return new ResponseEntity<>(
                annotatorModelAssembler.toCollectionModel(annotatorEntities),
                HttpStatus.OK);
    }



    @PutMapping("/api/v1/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user, @PathVariable UUID id){
        UserEntity userToUpdate = user;
        userToUpdate.setUserUUID(id);
        userRepository.save(userToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + userToUpdate);
        }
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

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id).map(userModelAssembler::toModel);

        if (user.isPresent()) {

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(Region.EU_CENTRAL_1)
                    .build();

            AdminGetUserResponse awsUser = AWSCognito.getUserByUsername(cognitoClient, "eu-central-1_1cFVgcU36", user.get().getUsername());
            cognitoClient.close();
            for (AttributeType attribute : awsUser.userAttributes()) {
                switch (attribute.name()){
                    case "email": user.get().setEmail(attribute.value());
                }
            }
        }

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

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
