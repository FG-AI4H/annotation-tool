package org.fgai4h.ap.helpers;

import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.catalog.model.TableModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.glue.GlueClient;
import software.amazon.awssdk.services.glue.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AWSGlue {

    private AWSGlue() {
        throw new IllegalStateException("Utility class");
    }

    static Logger logger = LoggerFactory.getLogger(AWSGlue.class);

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
            logger.info(e.awsErrorDetails().errorMessage());
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

                if(table.parameters().get("DEPRECATED_BY_CRAWLER") == null) {
                    databasesModel.add(DatasetModel.builder()
                            .name(table.name())
                            .description(table.description())
                            .linked(true)
                            .dataCatalogId(dataCatalogModel.getDataCatalogUUID())
                            .catalogLocation("AWS: " + dataCatalogModel.getAwsRegion())
                            .storageLocation(table.storageDescriptor().location())
                            .createdAt(LocalDateTime.ofInstant(table.createTime(), ZoneOffset.UTC))
                            .build());
                }
            }

        } catch (GlueException e) {
            logger.info(e.awsErrorDetails().errorMessage());
        }

        glueClient.close();
        return databasesModel;
    }

    public static Optional<TableModel> getGlueTable(DataCatalogModel dataCatalogModel, String tableName ) {

        GlueClient glueClient = GlueClient.builder()
                .region(Region.of(dataCatalogModel.getAwsRegion()))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        Optional<TableModel> result = Optional.empty();

        try {
            GetTableRequest tableRequest = GetTableRequest.builder()
                    .databaseName(dataCatalogModel.getDatabaseName())
                    .name(tableName)
                    .build();

            GetTableResponse tableResponse = glueClient.getTable(tableRequest);
            TableModel tableModel = TableModel.builder()
                    .name(tableResponse.table().name())
                    .description(tableResponse.table().description())
                    .build();
            result = Optional.of(tableModel);

        } catch (GlueException e) {
            logger.info(e.awsErrorDetails().errorMessage());
        }

        glueClient.close();
        return result;
    }

}
