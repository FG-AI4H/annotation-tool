package org.fgai4h.ap.helpers;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.List;

public class AWSCognito {

    private AWSCognito() {
        throw new IllegalStateException("Utility class");
    }

    public static void listAllUserPools(CognitoIdentityProviderClient cognitoClient ) {

        try {
            ListUserPoolsRequest request = ListUserPoolsRequest.builder()
                    .maxResults(10)
                    .build();

            ListUserPoolsResponse response = cognitoClient.listUserPools(request);
            response.userPools().forEach(userpool -> {
                        System.out.println("User pool " + userpool.name() + ", User ID " + userpool.id() );
                    }
            );

        } catch (CognitoIdentityProviderException e){
            e.getStackTrace();
        }
    }

    public static List<UserType> getAllUsers(CognitoIdentityProviderClient cognitoClient, String userPoolId ) {

        try {
            // List all users
            ListUsersRequest usersRequest = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .build();

            ListUsersResponse response = cognitoClient.listUsers(usersRequest);
            return response.users();

        } catch (CognitoIdentityProviderException e){
            e.getStackTrace();
        }
        return new ArrayList<>();
    }

    // Shows how to list users by using a filter.
    public static AdminGetUserResponse getUserByUsername(CognitoIdentityProviderClient cognitoClient, String userPoolId, String username ) {

        try {

            AdminGetUserRequest adminGetUserRequest = AdminGetUserRequest.builder().username(username).userPoolId(userPoolId).build();
            return cognitoClient.adminGetUser(adminGetUserRequest);

        } catch (CognitoIdentityProviderException e){
            e.getStackTrace();
        }
        return null;
    }
}
