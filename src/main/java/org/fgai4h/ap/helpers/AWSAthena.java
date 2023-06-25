package org.fgai4h.ap.helpers;

import org.fgai4h.ap.api.model.DatasetDto;
import org.fgai4h.ap.api.model.DatasetMetadataDto;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AWSAthena {
    private static final Logger logger = LoggerFactory.getLogger(AWSAthena.class);
    private static final long SLEEP_AMOUNT_IN_MS = 1000;

    public static List<DatasetDto> getCatalogDatasets(DataCatalogModel dataCatalogModel){
        AthenaClient athenaClient = AthenaClientFactory.createClient(Region.of(dataCatalogModel.getAwsRegion()));

        try {
            String queryExecutionId = submitAthenaQuery(athenaClient,dataCatalogModel);

            logger.info("Query submitted: " + System.currentTimeMillis());

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

    private static List<DatasetDto> processResultRows(AthenaClient athenaClient, String queryExecutionId, DataCatalogModel dataCatalogModel) {

        List<DatasetDto> datasetModelList = new ArrayList<>();
        GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

        List<Row> rows;
        for (GetQueryResultsResponse result : getQueryResultsResults) {
            rows = result.resultSet().rows();

            for (Row myRow : rows.subList(1, rows.size())) { // skip first row – column names
                List<Datum> allData = myRow.data();
                DatasetDto datasetModel = new DatasetDto();
                DatasetMetadataDto datasetMetadataDto = new DatasetMetadataDto();

                datasetModel.setName(allData.get(23).varCharValue());
                datasetModel.setCatalogLocation(dataCatalogModel.getAwsRegion());
                datasetModel.setDataCatalogId(dataCatalogModel.getDataCatalogUUID());

                datasetMetadataDto.setDataAcquisitionSensingModality(allData.get(6).varCharValue());
                datasetMetadataDto.setDataAcceptanceStandardsCompliance("");
                datasetMetadataDto.setDataAcquisitionSensingDeviceType("");
                datasetMetadataDto.setDataAcquisitionSensingModality("");
                datasetMetadataDto.setDataAnnotationProcessTool("");
                datasetMetadataDto.setDataAssumptionsConstraintsDependencies("");
                datasetMetadataDto.setDataBiasAndVarianceMinimization("");
                datasetMetadataDto.setDataCollectionFundingAgency("");
                datasetMetadataDto.setDataDimension("");
                datasetMetadataDto.setDataCollectionPeriod("");
                datasetMetadataDto.setDataCollectionPlace("");
                datasetMetadataDto.setDataOwnerId(UUID.fromString(allData.get(14).varCharValue()));

                datasetMetadataDto.setDataExclusionCriteria("");
                datasetMetadataDto.setDataRegistryUrl("");
                datasetMetadataDto.setVersion("");
                datasetMetadataDto.setDataPreprocessingTechniques("");
                datasetMetadataDto.setDataPrivacyDeIdentificationProtocol("");
                datasetMetadataDto.setDataResolutionPrecision("");
                datasetMetadataDto.setDataSafetySecurityProtocol("");
                datasetMetadataDto.setDataSampleSize("");
                datasetMetadataDto.setDataSource(allData.get(23).varCharValue());
                datasetMetadataDto.setDataUpdateVersion("");
                datasetMetadataDto.setTrainTuningEvalDatasetPartitioningRatio("");
                datasetMetadataDto.setDataType(allData.get(23).varCharValue());
                datasetMetadataDto.setDataSamplingRate("");

                datasetModel.setMetadata(datasetMetadataDto);
                datasetModelList.add(datasetModel);
            }
        }

        return datasetModelList;
    }

    public static void listDataCatalogs(DataCatalogModel dataCatalogModel) {
        try{
            AthenaClient athenaClient = AthenaClientFactory.createClient(Region.of(dataCatalogModel.getAwsRegion()));

            ListDataCatalogsRequest listDataCatalogsRequest = ListDataCatalogsRequest.builder()
                    .build();

            ListDataCatalogsResponse listDataCatalogsResponse = athenaClient.listDataCatalogs(listDataCatalogsRequest);
            System.out.println(listDataCatalogsResponse.dataCatalogsSummary().toString());


        } catch (AthenaException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
