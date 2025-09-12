package com.example.database.repository;


import com.example.database.entity.TaskStateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStateRepository  extends JpaRepository<TaskStateModel, Long> {

    List<TaskStateModel> findTaskStateModelByProjectId(Long project_id);
}
