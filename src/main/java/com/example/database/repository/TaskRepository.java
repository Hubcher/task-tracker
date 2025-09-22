package com.example.database.repository;

import com.example.database.entity.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findTaskModelByTaskStateId(Long taskStateId);

    Optional<TaskModel> findByIdAndTaskStateId(Long taskId, Long taskStateId);
}
