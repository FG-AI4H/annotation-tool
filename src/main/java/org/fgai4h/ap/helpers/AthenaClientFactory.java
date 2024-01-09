package org.fgai4h.ap.helpers;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;

class AthenaClientFactory {
    static AthenaClient createClient(Region awsRegion) {
        return AthenaClient.builder()
                .region(awsRegion)
                //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
