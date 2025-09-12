package com.example.service;


import com.example.aop.CheckProjectExists;
import com.example.api.dto.TaskStateDto;
import com.example.api.factories.TaskStateDtoMapper;
import com.example.database.repository.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskStateService {

    private final TaskStateDtoMapper taskStateDtoMapper;
    private final TaskStateRepository taskStateRepository;

    @CheckProjectExists
    public List<TaskStateDto> getTaskStates(Long projectId) {

        return taskStateRepository
                .findTaskStateModelByProjectId(projectId)
                .stream()
                .map(taskStateDtoMapper::makeTaskStateDto)
                .collect(Collectors.toList());
    }
}
