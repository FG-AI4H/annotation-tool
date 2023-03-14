package org.fgai4h.ap.domain.dataset.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRepository;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.model.UserRole;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.fgai4h.ap.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final DatasetModelAssembler datasetModelAssembler;
    private final DatasetMapper datasetMapper;
    private final UserRepository userRepository;
    private final DatasetRoleService datasetRoleService;
    private final UserService userService;

    public List<DatasetModel> getAllDatasets(String userUUID) {
        return datasetRepository.findAllByUserId(userUUID)
                .stream()
                .map(datasetModelAssembler::toModel).collect(Collectors.toList());
    }

    public DatasetModel addDataset(DatasetModel datasetModel, String userIdpId) {
        DatasetEntity newDataset = datasetRepository.save(datasetMapper.toDatasetEntity(datasetModel));

        UserModel currentUser = userService.getUserByIdpId(userIdpId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "User", "idpId", userIdpId));

        DatasetRoleModel datasetRole = new DatasetRoleModel();
        datasetRole.setDataset(datasetModelAssembler.toModel(newDataset));
        datasetRole.setUserRole(UserRole.OWNER);
        datasetRole.setUser(currentUser);

        datasetRoleService.addDatasetRole(datasetRole);

        return datasetModelAssembler.toModel(newDataset);
    }

    public Optional<DatasetModel> getDatasetById(UUID datasetId) {
        return Optional.ofNullable(datasetRepository
                .findById(datasetId)
                .map(datasetModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Dataset", "id", datasetId)));
    }

    public DatasetModel findById(UUID datasetId) {
        return getDatasetById(datasetId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Dataset", "id", datasetId));
    }

    public void updateDataset(DatasetModel datasetModel) {
        datasetRepository.save(datasetMapper.toDatasetEntity(datasetModel));
    }

    public void deleteCampaignById(UUID datasetId) {
        datasetRepository.deleteById(datasetId);
    }
}
