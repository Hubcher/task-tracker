package com.example.api.controller;


import com.example.api.dto.AckDto;
import com.example.api.dto.TaskDto;
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
    public List<TaskDto> getTasks(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId) {
        return taskService.getTasks(projectId, taskStateId);
    }

    @PostMapping
    public TaskDto createTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestParam(name = "task_name") String taskName) {
        return taskService.createTask(projectId, taskStateId, taskName);
    }

    @DeleteMapping(DELETE_TASK)
    public AckDto deleteTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @PathVariable(name = "task_id") Long taskId) {
        return taskService.deleteTask(projectId,taskStateId, taskId);
    }

    @PatchMapping(UPDATE)
    public TaskDto updateTask(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @PathVariable(name = "task_id") Long taskId,
            @RequestParam(name = "task_name") String taskName) {
        return taskService.updateTask(projectId,taskStateId, taskId, taskName);

    }








}
