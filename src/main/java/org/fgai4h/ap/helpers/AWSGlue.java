package org.fgai4h.ap.helpers;

import lombok.AllArgsConstructor;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueClient;
import software.amazon.awssdk.services.glue.model.Database;
import software.amazon.awssdk.services.glue.model.GetDatabasesRequest;
import software.amazon.awssdk.services.glue.model.GetDatabasesResponse;
import software.amazon.awssdk.services.glue.model.GlueException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AWSGlue {


    public static List<DatasetModel> getAllDatabases() {

        GlueClient glueClient = GlueClient.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        List<DatasetModel> databasesModel = new ArrayList<>();
        try {
            GetDatabasesRequest databasesRequest = GetDatabasesRequest.builder()
                    .build();

            GetDatabasesResponse response = glueClient.getDatabases(databasesRequest);
            List<Database> databases = response.databaseList();
            for (Database database: databases) {
                databasesModel.add(DatasetModel.builder()
                        .name(database.name())
                        .description(database.description())
                        .linked(true)
                        .catalogLocation("AWS: "+ Region.EU_CENTRAL_1.toString())
                        .storageLocation(database.locationUri())
                        .createdAt(LocalDateTime.ofInstant(database.createTime(), ZoneId.of("Europe/Berlin")))
                        .build());
            }

        } catch (GlueException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        glueClient.close();
        return databasesModel;
    }

}
