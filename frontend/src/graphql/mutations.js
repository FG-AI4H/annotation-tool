/* eslint-disable */
// this is an auto generated file. This will be overwritten

export const createDataset = /* GraphQL */ `
  mutation CreateDataset(
    $input: CreateDatasetInput!
    $condition: ModelDatasetConditionInput
  ) {
    createDataset(input: $input, condition: $condition) {
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
export const updateDataset = /* GraphQL */ `
  mutation UpdateDataset(
    $input: UpdateDatasetInput!
    $condition: ModelDatasetConditionInput
  ) {
    updateDataset(input: $input, condition: $condition) {
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
export const deleteDataset = /* GraphQL */ `
  mutation DeleteDataset(
    $input: DeleteDatasetInput!
    $condition: ModelDatasetConditionInput
  ) {
    deleteDataset(input: $input, condition: $condition) {
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
