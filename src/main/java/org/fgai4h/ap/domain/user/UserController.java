package org.fgai4h.ap.domain.user;

import org.fgai4h.ap.helpers.AWSCognito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final AnnotatorRepository annotatorRepository;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;

    public UserController(UserRepository userRepository, UserModelAssembler userModelAssembler, AnnotatorRepository annotatorRepository, AnnotatorModelAssembler annotatorModelAssembler,UserModelAWSAssembler userModelAWSAssembler){
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
        this.annotatorRepository = annotatorRepository;
        this.annotatorModelAssembler = annotatorModelAssembler;
        this.userModelAWSAssembler = userModelAWSAssembler;
    }

    @GetMapping("/annotators")
    public ResponseEntity<CollectionModel<AnnotatorModel>> getAllAnnotators()
    {
        List<AnnotatorEntity> annotatorEntities = annotatorRepository.findAll();
        return new ResponseEntity<>(
                annotatorModelAssembler.toCollectionModel(annotatorEntities),
                HttpStatus.OK);
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers()
    {

        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        List<UserType> awsUsers = AWSCognito.getAllUsers(cognitoClient,"eu-central-1_1cFVgcU36") ;
        cognitoClient.close();

        return new ResponseEntity<>(
                userModelAWSAssembler.toCollectionModel(awsUsers),
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
    public ResponseEntity<UserModel> addUser(@RequestBody UserEntity newUser){

        return null;
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") UUID id) {
        return null;
    }

    @GetMapping("/annotators/{id}")
    public ResponseEntity<AnnotatorModel> getAnnotatorById(@PathVariable("id") UUID id) {
        return annotatorRepository.findById(id)
                .map(annotatorModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
