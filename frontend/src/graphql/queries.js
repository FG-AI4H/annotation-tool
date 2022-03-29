/* eslint-disable */
// this is an auto generated file. This will be overwritten

export const getDataset = /* GraphQL */ `
  query GetDataset($id: ID!) {
    getDataset(id: $id) {
      id
      name
      description
      storageLocation
      metadata {
        version
        dataOwner
        dataSource
        dataSampleSize
        dataType
        dataUpdateVersion
        dataAcquisitionSensingModality
        dataAcquisitionSensingDeviceType
        dataCollectionPlace
        dataCollectionPeriod
        datCollectionAuthorsAgency
        dataCollectionFundingAgency
        dataSamplingRate
        dataDimension
        dataResolutionPrecision
        dataPrivacyDeIdentificationProtocol
        dataSafetySecurityProtocol
        dataAssumptionsConstraintsDependencies
        dataExclusionCriteria
        dataAcceptanceStandardsCompliance
        dataPreprocessingTechniques
        dataAnnotationProcessTool
        dataBiasAndVarianceMinimization
        trainTuningEvalDatasetPartitioningRatio
        dataRegistryURL
      }
      createdAt
      updatedAt
    }
  }
`;
export const listDatasets = /* GraphQL */ `
  query ListDatasets(
    $filter: ModelDatasetFilterInput
    $limit: Int
    $nextToken: String
  ) {
    listDatasets(filter: $filter, limit: $limit, nextToken: $nextToken) {
      items {
        id
        name
        description
        storageLocation
        createdAt
        updatedAt
      }
      nextToken
    }
  }
`;
