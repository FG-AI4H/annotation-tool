package org.fgai4h.ap.domain.dataset.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.repository.DatasetRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final DatasetModelAssembler datasetModelAssembler;

}
