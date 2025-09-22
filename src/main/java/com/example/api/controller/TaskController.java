package com.example.api.controller;


import com.example.api.dto.AckDto;
import com.example.api.dto.TaskCreateDto;
import com.example.api.dto.TaskReadDto;
import com.example.api.dto.TaskUpdateDto;
import com.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects/{project_id}/task-states/{task_state_id}/tasks")
public class TaskController {

    private final TaskService taskService;

    private static final String DELETE_TASK = "/{task_id}";
    private static final String UPDATE = "/{task_id}";

    @GetMapping
    public List<TaskReadDto> getTasks(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId) {
        return taskService.getTasks(projectId, taskStateId);
    }

    @PostMapping
    public TaskReadDto createTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestBody TaskCreateDto dto) {
        return taskService.createTask(projectId, taskStateId, dto);
    }

    @DeleteMapping(DELETE_TASK)
    public AckDto deleteTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @PathVariable(name = "task_id") Long taskId) {
        return taskService.deleteTask(projectId,taskStateId, taskId);
    }

    @PatchMapping(UPDATE)
    public TaskReadDto updateTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @PathVariable(name = "task_id") Long taskId,
            @RequestBody TaskUpdateDto dto) {
        return taskService.updateTask(projectId,taskStateId, taskId, dto);

    }








}
