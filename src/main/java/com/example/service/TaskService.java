package com.example.service;

import com.example.aop.CheckProjectExists;
import com.example.aop.CheckTaskExists;
import com.example.aop.CheckTaskStateExists;
import com.example.api.dto.AckDto;
import com.example.api.dto.TaskCreateDto;
import com.example.api.dto.TaskReadDto;
import com.example.api.dto.TaskUpdateDto;
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
    public List<TaskReadDto> getTasks(Long projectId, Long taskStateId) {
        return taskRepository
                .findTaskModelByTaskStateId(taskStateId)
                .stream()
                .map(taskDtoMapper::makeTaskDto)
                .collect(Collectors.toList());
    }

    @CheckProjectExists
    @CheckTaskStateExists
    public TaskReadDto createTask(Long projectId, Long taskStateId, TaskCreateDto dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BadRequestException("Task name can't be empty");
        }

        TaskStateModel taskState = taskStateRepository.getReferenceById(taskStateId);

        for (TaskModel task : taskState.getTasks()) {
            if (task.getName().equalsIgnoreCase(dto.getName())) {
                throw new BadRequestException("Task \"%s\" is already exists".formatted(dto.getName()));
            }
        }

        TaskModel task = taskDtoMapper.makeTaskEntity(dto, taskState);
        TaskModel savedTask = taskRepository.saveAndFlush(task);

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
    public TaskReadDto updateTask(Long projectId, Long taskStateId, Long taskId, TaskUpdateDto dto) {

        TaskModel task = taskRepository.findByIdAndTaskStateId(taskId, taskStateId)
                .orElseThrow(() -> new BadRequestException(
                        "Task \"%d\" not found in TaskState \"%d\"".formatted(taskId, taskStateId)));

        taskDtoMapper.updateTaskEntity(dto, task);

        var savedTask = taskRepository.saveAndFlush(task);

        return taskDtoMapper.makeTaskDto(savedTask);
    }

}
