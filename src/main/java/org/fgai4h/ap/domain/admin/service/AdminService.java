package org.fgai4h.ap.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.mapper.UserMapper;
import org.fgai4h.ap.domain.user.mapper.UserModelAWSAssembler;
import org.fgai4h.ap.domain.user.mapper.UserModelAssembler;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.fgai4h.ap.helpers.AWSCognito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;
    private final UserModelAWSAssembler userModelAWSAssembler;
    private final UserMapper userMapper;

    public List<UserModel> retrieveAllUsers() {
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        List<UserType> awsUsers = AWSCognito.getAllUsers(cognitoClient,"eu-central-1_1cFVgcU36") ;
        cognitoClient.close();

        List<UserModel> userList = new ArrayList<>();
        CollectionModel<UserModel> userModelCollection = userModelAWSAssembler.toCollectionModel(awsUsers);
        for (UserModel next : userModelCollection) {
            UserEntity userEntity = userRepository.findByIdpId(next.getIdpId())
                    .orElseGet(() -> userRepository.save(userMapper.toUserEntity(next)));
            next.setUserUUID(userEntity.getUserUUID());
            userList.add(next);
        }

        return userList;

    }

}
