package org.fgai4h.ap.domain.admin;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.mapper.*;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.repository.UserRepository;
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
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final AnnotatorModelAssembler annotatorModelAssembler;
    private final ReviewerModelAssembler reviewerModelAssembler;
    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers()
    {

        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        List<UserType> awsUsers = AWSCognito.getAllUsers(cognitoClient,"eu-central-1_1cFVgcU36") ;
        cognitoClient.close();

        CollectionModel<UserModel> userModelCollection = userModelAWSAssembler.toCollectionModel(awsUsers);
        for (UserModel next : userModelCollection) {
            UserEntity userEntity = userRepository.findByIdpId(next.getIdpID()).get();

            // If user does not exists in local DB
            if (userEntity == null) {
                userEntity = userRepository.save(userMapper.toUserEntity(next));
            }

            next.setAnnotatorRole(annotatorModelAssembler.toModel(userEntity.getAnnotatorRole()));
            next.setReviewerRole(reviewerModelAssembler.toModel(userEntity.getReviewerRole()));
            next.setUserUUID(userEntity.getUserUUID());

        }

        return new ResponseEntity<>(
                userModelCollection,
                HttpStatus.OK);
    }

    public Optional<UserModel> getUserById(final UUID userId) {
        Optional<UserModel> userModel = Optional.ofNullable(userRepository
                .findById(userId)
                .map(userModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "User", "id", userId)));

        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        AdminGetUserResponse awsUser = AWSCognito.getUserByUsername(cognitoClient, "eu-central-1_1cFVgcU36", userModel.get().getUsername());
        cognitoClient.close();
        for (AttributeType attribute : awsUser.userAttributes()) {
            switch (attribute.name()){
                case "email": userModel.get().setEmail(attribute.value());
            }
        }

        return userModel;

    }
}
