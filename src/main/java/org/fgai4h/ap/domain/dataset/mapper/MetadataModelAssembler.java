package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.controller.DatasetController;
import org.fgai4h.ap.domain.dataset.entity.DatasetMetadataEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetadataModelAssembler extends RepresentationModelAssemblerSupport<DatasetMetadataEntity, DatasetMetadataModel> {

    public MetadataModelAssembler() {
        super(DatasetController.class, DatasetMetadataModel.class);
    }

    @Override
    public DatasetMetadataModel toModel(DatasetMetadataEntity entity)
    {
        DatasetMetadataModel datasetMetadataModel = instantiateModel(entity);

        if (isNull(entity))
            return datasetMetadataModel;

        datasetMetadataModel.add(linkTo(
                methodOn(DatasetController.class)

                        .getMetadataById(entity.getMetadataUUID()))
                .withSelfRel());


        datasetMetadataModel.setMetadataUUID(entity.getMetadataUUID());
        datasetMetadataModel.setDataAcquisitionSensingModality(entity.getDataAcquisitionSensingModality());
        datasetMetadataModel.setDataAcceptanceStandardsCompliance(entity.getDataAcceptanceStandardsCompliance());
        datasetMetadataModel.setDataAcquisitionSensingDeviceType(entity.getDataAcquisitionSensingDeviceType());
        datasetMetadataModel.setDataAcquisitionSensingModality(entity.getDataAcquisitionSensingModality());
        datasetMetadataModel.setDataAnnotationProcessTool(entity.getDataAnnotationProcessTool());
        datasetMetadataModel.setDataAssumptionsConstraintsDependencies(entity.getDataAssumptionsConstraintsDependencies());
        datasetMetadataModel.setDataBiasAndVarianceMinimization(entity.getDataBiasAndVarianceMinimization());
        datasetMetadataModel.setDataCollectionFundingAgency(entity.getDataCollectionFundingAgency());
        datasetMetadataModel.setDataDimension(entity.getDataDimension());
        datasetMetadataModel.setDataCollectionPeriod(entity.getDataCollectionPeriod());
        datasetMetadataModel.setDataCollectionPlace(entity.getDataCollectionPlace());
        datasetMetadataModel.setDataOwner(entity.getDataOwner());
        datasetMetadataModel.setDataExclusionCriteria(entity.getDataExclusionCriteria());
        datasetMetadataModel.setDataRegistryURL(entity.getDataRegistryURL());
        datasetMetadataModel.setVersion(entity.getVersion());
        datasetMetadataModel.setDataPreprocessingTechniques(entity.getDataPreprocessingTechniques());
        datasetMetadataModel.setDataPrivacyDeIdentificationProtocol(entity.getDataPrivacyDeIdentificationProtocol());
        datasetMetadataModel.setDataResolutionPrecision(entity.getDataResolutionPrecision());
        datasetMetadataModel.setDataSafetySecurityProtocol(entity.getDataSafetySecurityProtocol());
        datasetMetadataModel.setDataSampleSize(entity.getDataSampleSize());
        datasetMetadataModel.setDataSource(entity.getDataSource());
        datasetMetadataModel.setDataUpdateVersion(entity.getDataUpdateVersion());
        datasetMetadataModel.setTrainTuningEvalDatasetPartitioningRatio(entity.getTrainTuningEvalDatasetPartitioningRatio());
        datasetMetadataModel.setDataType(entity.getDataType());
        datasetMetadataModel.setDataSamplingRate(entity.getDataSamplingRate());

        return datasetMetadataModel;
    }

}

