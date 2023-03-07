package org.fgai4h.ap.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.mapper.UserModelAssembler;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.fgai4h.ap.helpers.AWSCognito;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;

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

    public void updateUser(UserModel userModel){
        UserEntity userEntity = userModelAssembler.toEntity(userModel);
        userRepository.save(userEntity);

    }
}
