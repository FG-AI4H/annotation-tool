package org.fgai4h.ap.domain.dataset;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MetadataModelAssembler extends RepresentationModelAssemblerSupport<MetadataEntity, MetadataModel> {

    public MetadataModelAssembler() {
        super(DatasetController.class, MetadataModel.class);
    }

    @Override
    public MetadataModel toModel(MetadataEntity entity)
    {
        MetadataModel metadataModel = instantiateModel(entity);

        if (isNull(entity))
            return metadataModel;

        metadataModel.add(linkTo(
                methodOn(DatasetController.class)

                        .getMetadataById(entity.getMetadataUUID()))
                .withSelfRel());


        metadataModel.setMetadataUUID(entity.getMetadataUUID());
        metadataModel.setDataAcquisitionSensingModality(entity.getDataAcquisitionSensingModality());
        metadataModel.setDataAcceptanceStandardsCompliance(entity.getDataAcceptanceStandardsCompliance());
        metadataModel.setDataAcquisitionSensingDeviceType(entity.getDataAcquisitionSensingDeviceType());
        metadataModel.setDataAcquisitionSensingModality(entity.getDataAcquisitionSensingModality());
        metadataModel.setDataAnnotationProcessTool(entity.getDataAnnotationProcessTool());
        metadataModel.setDataAssumptionsConstraintsDependencies(entity.getDataAssumptionsConstraintsDependencies());
        metadataModel.setDataBiasAndVarianceMinimization(entity.getDataBiasAndVarianceMinimization());
        metadataModel.setDataCollectionFundingAgency(entity.getDataCollectionFundingAgency());
        metadataModel.setDataDimension(entity.getDataDimension());
        metadataModel.setDataCollectionPeriod(entity.getDataCollectionPeriod());
        metadataModel.setDataCollectionPlace(entity.getDataCollectionPlace());
        metadataModel.setDataOwner(entity.getDataOwner());
        metadataModel.setDataExclusionCriteria(entity.getDataExclusionCriteria());
        metadataModel.setDataRegistryURL(entity.getDataRegistryURL());
        metadataModel.setVersion(entity.getVersion());
        metadataModel.setDataPreprocessingTechniques(entity.getDataPreprocessingTechniques());
        metadataModel.setDataPrivacyDeIdentificationProtocol(entity.getDataPrivacyDeIdentificationProtocol());
        metadataModel.setDataResolutionPrecision(entity.getDataResolutionPrecision());
        metadataModel.setDataSafetySecurityProtocol(entity.getDataSafetySecurityProtocol());
        metadataModel.setDataSampleSize(entity.getDataSampleSize());
        metadataModel.setDataSource(entity.getDataSource());
        metadataModel.setDataUpdateVersion(entity.getDataUpdateVersion());
        metadataModel.setTrainTuningEvalDatasetPartitioningRatio(entity.getTrainTuningEvalDatasetPartitioningRatio());
        metadataModel.setDataType(entity.getDataType());
        metadataModel.setDataSamplingRate(entity.getDataSamplingRate());

        return metadataModel;
    }

}

