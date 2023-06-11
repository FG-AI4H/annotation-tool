package org.fgai4h.ap.helpers;

import lombok.AllArgsConstructor;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueClient;
import software.amazon.awssdk.services.glue.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AWSGlue {


    public static List<DatasetModel> getAllDatabases(DataCatalogModel dataCatalogModel) {

        GlueClient glueClient = GlueClient.builder()
                .region(Region.of(dataCatalogModel.getAwsRegion()))
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
                        .catalogLocation("AWS: "+ dataCatalogModel.getAwsRegion())
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

    public static List<DatasetModel> getAllTables(DataCatalogModel dataCatalogModel) {

        GlueClient glueClient = GlueClient.builder()
                .region(Region.of(dataCatalogModel.getAwsRegion()))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        List<DatasetModel> databasesModel = new ArrayList<>();
        try {
            GetTablesRequest tablesRequest = GetTablesRequest.builder()
                    .databaseName(dataCatalogModel.getDatabaseName())
                    .build();

            GetTablesResponse response = glueClient.getTables(tablesRequest);
            List<Table> tables = response.tableList();
            for (Table table: tables) {
                databasesModel.add(DatasetModel.builder()
                        .name(table.name())
                        .description(table.description())
                        .linked(true)
                        .catalogLocation("AWS: "+ dataCatalogModel.getAwsRegion())
                        .storageLocation(table.storageDescriptor().location())
                        .createdAt(LocalDateTime.ofInstant(table.createTime(), ZoneId.of("Europe/Berlin")))
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
