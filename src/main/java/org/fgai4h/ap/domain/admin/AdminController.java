package org.fgai4h.ap.domain.admin;

import org.fgai4h.ap.domain.user.*;
import org.fgai4h.ap.helpers.AWSCognito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final AnnotatorRepository annotatorRepository;
    private final ReviewerRepository reviewerRepository;
    private final SupervisorModelAssembler supervisorModelAssembler;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;
    private final ReviewerModelAssembler reviewerModelAssembler;
    private final SupervisorRepository supervisorRepository;

    public AdminController(UserRepository userRepository, UserModelAssembler userModelAssembler, AnnotatorRepository annotatorRepository, AnnotatorModelAssembler annotatorModelAssembler,UserModelAWSAssembler userModelAWSAssembler, ReviewerModelAssembler reviewerModelAssembler, ReviewerRepository reviewerRepository, SupervisorModelAssembler supervisorModelAssembler, SupervisorRepository supervisorRepository){
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

    @GetMapping("/users")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers()
    {
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        List<UserType> awsUsers = AWSCognito.getAllUsers(cognitoClient,"eu-central-1_1cFVgcU36") ;
        cognitoClient.close();

        CollectionModel<UserModel> userModelCollection = userModelAWSAssembler.toCollectionModel(awsUsers);
        Iterator<UserModel> iterator = userModelCollection.iterator();
        while(iterator.hasNext()) {
            UserModel next = iterator.next();
            UserEntity userEntity = userRepository.findByIdpId(next.getIdpID());

            // If user exists in local DB
            if(userEntity == null){
                userEntity = userRepository.save(userModelAssembler.toEntity(next));
            }

            next.setAnnotatorRole(annotatorModelAssembler.toModel(userEntity.getAnnotatorRole()));
            next.setReviewerRole(reviewerModelAssembler.toModel(userEntity.getReviewerRole()));
            next.setUserUUID(userEntity.getUserUUID());

        }

        return new ResponseEntity<>(
                userModelCollection,
                HttpStatus.OK);
    }
}
