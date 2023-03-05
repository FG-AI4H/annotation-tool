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
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;
    private final ReviewerModelAssembler reviewerModelAssembler;

    public AdminController(UserRepository userRepository, UserModelAssembler userModelAssembler, AnnotatorModelAssembler annotatorModelAssembler,UserModelAWSAssembler userModelAWSAssembler, ReviewerModelAssembler reviewerModelAssembler){
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
        this.annotatorModelAssembler = annotatorModelAssembler;
        this.userModelAWSAssembler = userModelAWSAssembler;
        this.reviewerModelAssembler = reviewerModelAssembler;
    }

    @GetMapping("/users")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers()
    {
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        List<UserType> awsUsers = AWSCognito.getAllUsers(cognitoClient,"eu-central-1_1cFVgcU36") ;
        cognitoClient.close();

        CollectionModel<UserModel> userModelCollection = userModelAWSAssembler.toCollectionModel(awsUsers);
        for (UserModel next : userModelCollection) {
            UserEntity userEntity = userRepository.findByIdpId(next.getIdpID());

            // If user does not exists in local DB
            if (userEntity == null) {
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
