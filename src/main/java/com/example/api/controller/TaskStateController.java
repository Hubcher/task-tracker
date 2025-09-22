package com.example.api.controller;


import com.example.api.dto.AckDto;
import com.example.api.dto.TaskStateDto;
import com.example.service.TaskStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TaskStateController {

    private final TaskStateService taskStateService;

    public static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    public static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    public static final String UPDATE_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    public static final String DELETE_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId) {
        return taskStateService.getTaskStates(projectId);
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(
            @PathVariable(name = "project_id") Long projectId,
            @RequestParam(name = "task_state_name") String taskStateName) {
        return taskStateService.createTaskState(projectId, taskStateName);
    }

    @DeleteMapping(DELETE_TASK_STATE)
    public AckDto deleteTaskState(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId) {
        return taskStateService.deleteTaskState(projectId, taskStateId);
    }

    @PatchMapping(UPDATE_TASK_STATE)
    public TaskStateDto updateTaskState(
            @PathVariable(name = "project_id") Long projectId,
            @PathVariable(name = "task_state_id") Long taskStateId,
            @RequestParam(name = "task_state_name") String taskStateName) {
        return taskStateService.updateTaskState(projectId, taskStateId, taskStateName);
    }


}
