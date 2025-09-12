package com.example.service;


import com.example.aop.CheckProjectExists;
import com.example.aop.CheckTaskStateExists;
import com.example.api.dto.AckDto;
import com.example.api.dto.TaskStateDto;
import com.example.api.exceptions.BadRequestException;
import com.example.api.factories.TaskStateDtoMapper;
import com.example.database.entity.ProjectModel;
import com.example.database.entity.TaskStateModel;
import com.example.database.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    @CheckProjectExists
    public List<TaskStateDto> getTaskStates(Long projectId) {

        return taskStateRepository
                .findTaskStateModelByProjectId(projectId)
                .stream()
                .map(taskStateDtoMapper::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @CheckProjectExists
    public TaskStateDto createTaskState(Long projectId, String taskStateName) {

        if (taskStateName.trim().isEmpty()) {
            throw new BadRequestException("Task state name can't be empty");
        }

        ProjectModel project = projectRepository.getReferenceById(projectId);

        for (TaskStateModel taskStates : project.getTaskStates()) {
            if (taskStates.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException("Task state \"%s\" already exist".formatted(taskStateName));
            }
        }

        TaskStateModel savedTaskState = taskStateRepository.saveAndFlush(
                TaskStateModel.builder()
                        .name(taskStateName)
                        .project(project)
                        .build()
        );

        return taskStateDtoMapper.makeTaskStateDto(savedTaskState);
    }

    @CheckProjectExists
    @CheckTaskStateExists
    public AckDto deleteTaskState(Long projectId, Long taskStateId) {
        taskStateRepository.deleteById(taskStateId);
        return AckDto.getAnswer(true);
    }
}
