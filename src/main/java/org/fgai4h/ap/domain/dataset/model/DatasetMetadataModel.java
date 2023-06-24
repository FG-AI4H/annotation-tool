package org.fgai4h.ap.domain.dataset.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "dataset")
@Relation(collectionRelation = "dataset")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetMetadataModel extends RepresentationModel<DatasetMetadataModel> {

    private UUID metadataUUID;

    private String dataCollectionAuthorsAgency;
    private String dataAcceptanceStandardsCompliance;
    private String dataAcquisitionSensingDeviceType;
    private String dataAcquisitionSensingModality;
    private String dataAnnotationProcessTool;
    private String dataAssumptionsConstraintsDependencies;
    private String dataBiasAndVarianceMinimization;
    private String dataCollectionFundingAgency;
    private String dataCollectionPeriod;
    private String dataCollectionPlace;
    private String dataDimension;
    private String dataExclusionCriteria;
    private UserModel dataOwner;
    private String dataPreprocessingTechniques;
    private String dataPrivacyDeIdentificationProtocol;
    private String dataRegistryURL;
    private String dataResolutionPrecision;
    private String dataSafetySecurityProtocol;
    private String dataSampleSize;
    private String dataSamplingRate;
    private String dataSource;
    private String dataType;
    private String dataUpdateVersion;
    private String trainTuningEvalDatasetPartitioningRatio;
    private String version;
}
