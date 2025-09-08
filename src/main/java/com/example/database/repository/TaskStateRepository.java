package com.example.database.repository;


import com.example.database.entity.TaskStateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStateRepository  extends JpaRepository<TaskStateModel, Long> {
}
