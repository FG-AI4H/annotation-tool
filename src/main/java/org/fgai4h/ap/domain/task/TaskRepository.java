package org.fgai4h.ap.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query("select t from TaskEntity t where t.assignee.userUUID = :userUUID")
    public List<TaskEntity> findMyTasks(@Param("userUUID") UUID userUUID);
}
