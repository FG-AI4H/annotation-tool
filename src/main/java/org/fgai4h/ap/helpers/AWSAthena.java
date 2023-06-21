package org.fgai4h.ap.helpers;

import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import java.util.ArrayList;
import java.util.List;

public class AWSAthena {

    private static final Logger logger = LoggerFactory.getLogger(AWSAthena.class);
    private static final long SLEEP_AMOUNT_IN_MS = 1000;

    private AWSAthena() {
        throw new IllegalStateException("Utility class");
    }

    public static List<DatasetModel> getCatalogDatasets(DataCatalogModel dataCatalogModel){
        AthenaClient athenaClient = AthenaClientFactory.createClient(Region.of(dataCatalogModel.getAwsRegion()));

        String queryExecutionId = submitAthenaQuery(athenaClient,dataCatalogModel);

        logger.info("Query submitted: " + System.currentTimeMillis());

        try {
            waitForQueryToComplete(athenaClient, queryExecutionId);
            logger.info("Query finished: " + System.currentTimeMillis());

            return processResultRows(athenaClient, queryExecutionId, dataCatalogModel);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new ArrayList<>();

    }

    private static String submitAthenaQuery(AthenaClient athenaClient, DataCatalogModel dataCatalogModel) {

        QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder()
                .catalog("AwsDataCatalog")
                .database(dataCatalogModel.getDatabaseName()).build();

        ResultConfiguration resultConfiguration = ResultConfiguration.builder()
                .outputLocation("s3://fgai4h-oci-athena-query-result-"+dataCatalogModel.getAwsRegion()).build();

        StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
                .queryString("SELECT * FROM \""+dataCatalogModel.getBucketName().replaceAll("-","_")+"\";")
                .queryExecutionContext(queryExecutionContext)
                .resultConfiguration(resultConfiguration).build();

        StartQueryExecutionResponse startQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest);

        return startQueryExecutionResponse.queryExecutionId();
    }

    private static void waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId) throws InterruptedException {

        GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryExecutionResponse getQueryExecutionResponse;

        boolean isQueryStillRunning = true;

        while (isQueryStillRunning) {
            getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
            String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();

            if (queryState.equals(QueryExecutionState.FAILED.toString())) {
                throw new RuntimeException("Query Failed to run with Error Message: " + getQueryExecutionResponse
                        .queryExecution().status().stateChangeReason());
            } else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
                throw new RuntimeException("Query was cancelled.");
            } else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
                isQueryStillRunning = false;
            } else {
                Thread.sleep(SLEEP_AMOUNT_IN_MS);
            }

            logger.info("Current Status is: " + queryState);
        }
    }

    private static List<DatasetModel> processResultRows(AthenaClient athenaClient, String queryExecutionId, DataCatalogModel dataCatalogModel) {

        List<DatasetModel> datasetModelList = new ArrayList<>();
        GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

        List<Row> rows;
        for (GetQueryResultsResponse result : getQueryResultsResults) {
            rows = result.resultSet().rows();

            for (Row myRow : rows.subList(1, rows.size())) { // skip first row – column names
                List<Datum> allData = myRow.data();
                DatasetModel datasetModel = new DatasetModel();
                DatasetMetadataModel datasetMetadataModel = new DatasetMetadataModel();

                datasetModel.setName(allData.get(22).varCharValue());
                datasetModel.setCatalogLocation(dataCatalogModel.getAwsRegion());

                datasetMetadataModel.setDataAcquisitionSensingModality(allData.get(6).varCharValue());
                datasetMetadataModel.setDataAcceptanceStandardsCompliance("");
                datasetMetadataModel.setDataAcquisitionSensingDeviceType("");
                datasetMetadataModel.setDataAcquisitionSensingModality("");
                datasetMetadataModel.setDataAnnotationProcessTool("");
                datasetMetadataModel.setDataAssumptionsConstraintsDependencies("");
                datasetMetadataModel.setDataBiasAndVarianceMinimization("");
                datasetMetadataModel.setDataCollectionFundingAgency("");
                datasetMetadataModel.setDataDimension("");
                datasetMetadataModel.setDataCollectionPeriod("");
                datasetMetadataModel.setDataCollectionPlace("");
                datasetMetadataModel.setDataOwner(allData.get(14).varCharValue());
                datasetMetadataModel.setDataExclusionCriteria("");
                datasetMetadataModel.setDataRegistryURL("");
                datasetMetadataModel.setVersion("");
                datasetMetadataModel.setDataPreprocessingTechniques("");
                datasetMetadataModel.setDataPrivacyDeIdentificationProtocol("");
                datasetMetadataModel.setDataResolutionPrecision("");
                datasetMetadataModel.setDataSafetySecurityProtocol("");
                datasetMetadataModel.setDataSampleSize("");
                datasetMetadataModel.setDataSource(allData.get(22).varCharValue());
                datasetMetadataModel.setDataUpdateVersion("");
                datasetMetadataModel.setTrainTuningEvalDatasetPartitioningRatio("");
                datasetMetadataModel.setDataType(allData.get(23).varCharValue());
                datasetMetadataModel.setDataSamplingRate("");

                datasetModel.setMetadata(datasetMetadataModel);
                datasetModelList.add(datasetModel);
            }
        }

        return datasetModelList;
    }
}
