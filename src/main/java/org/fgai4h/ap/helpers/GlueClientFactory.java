package org.fgai4h.ap.helpers;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueClient;

public class GlueClientFactory {

    static GlueClient createClient(Region awsRegion) {
        return GlueClient.builder()
                .region(awsRegion)
                //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
