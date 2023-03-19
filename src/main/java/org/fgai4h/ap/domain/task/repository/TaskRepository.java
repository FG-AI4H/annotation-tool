package org.fgai4h.ap.domain.task.repository;

import org.fgai4h.ap.domain.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query("select t from TaskEntity t where t.assignee.idpId = :idpID")
    public List<TaskEntity> findMyTasks(@Param("idpID") String idpID);
}
