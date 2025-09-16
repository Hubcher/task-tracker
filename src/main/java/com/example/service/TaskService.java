package com.example.service;

import com.example.aop.CheckProjectExists;
import com.example.aop.CheckTaskExists;
import com.example.aop.CheckTaskStateExists;
import com.example.api.dto.AckDto;
import com.example.api.dto.TaskDto;
import com.example.api.exceptions.BadRequestException;
import com.example.api.factories.TaskDtoMapper;
import com.example.database.entity.TaskModel;
import com.example.database.entity.TaskStateModel;
import com.example.database.repository.TaskRepository;
import com.example.database.repository.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskDtoMapper taskDtoMapper;
    private final TaskStateRepository taskStateRepository;

    @CheckProjectExists
    @CheckTaskStateExists
    public List<TaskDto> getTasks(Long projectId, Long taskStateId) {
        return taskRepository
                .findTaskModelByTaskStateId(taskStateId)
                .stream()
                .map(taskDtoMapper::makeTaskDto)
                .collect(Collectors.toList());
    }

    // TODO сделать через RequestBody и создать TaskCreateDto и TaskUpdateDto
    @CheckProjectExists
    @CheckTaskStateExists
    public TaskDto createTask(Long projectId, Long taskStateId, String taskName) {

        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("Task name can't be empty");
        }

        TaskStateModel taskState = taskStateRepository.getReferenceById(taskStateId);

        for (TaskModel task : taskState.getTasks()) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                throw new BadRequestException("Task \"%s\" is already exists".formatted(taskName));
            }
        }

        TaskModel savedTask = taskRepository.saveAndFlush(
                TaskModel.builder()
                        .taskState(taskState)
                        .name(taskName)
                        .build()
        );

        return taskDtoMapper.makeTaskDto(savedTask);
    }

    @CheckProjectExists
    @CheckTaskStateExists
    @CheckTaskExists
    public AckDto deleteTask(Long projectId, Long taskStateId, Long taskId) {

        taskRepository.deleteById(taskId);

        return AckDto.getAnswer(true);

    }


    @CheckProjectExists
    @CheckTaskStateExists
    @CheckTaskExists
    public TaskDto updateTask(Long projectId, Long taskStateId, Long taskId, String taskName) {

        if (taskName.trim().isEmpty()) {
            throw new BadRequestException("Task name can't be empty");
        }

        TaskModel task = taskRepository.findByIdAndTaskStateId(taskId, taskStateId)
                .orElseThrow(() -> new BadRequestException(
                        "Task \"%d\" not found in TaskState \"%d\"".formatted(taskId, taskStateId)));

        task.setName(taskName);

        var savedTask = taskRepository.saveAndFlush(task);

        return taskDtoMapper.makeTaskDto(savedTask);
    }

}
