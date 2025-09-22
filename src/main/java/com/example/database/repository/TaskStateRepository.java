package com.example.database.repository;


import com.example.database.entity.TaskStateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskStateRepository  extends JpaRepository<TaskStateModel, Long> {

    Optional<TaskStateModel> findTaskStateModelByProjectIdAndNameContainsIgnoreCase(
            Long projectId, String taskStateName);

    List<TaskStateModel> findTaskStateModelByProjectId(Long projectId);

    Optional<TaskStateModel> findByIdAndProjectId(Long taskStateId, Long projectId);

    @Override
    Optional<TaskStateModel> findById(Long id);
}
