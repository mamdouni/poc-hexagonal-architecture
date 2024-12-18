package com.decathlon.domain_name.biz_ctx.infra.primary.rest.user.controllers;

import com.decathlon.domain_name.biz_ctx.application.user.usecase.input.port.DisplayUsersUseCases;
import com.decathlon.domain_name.biz_ctx.application.user.usecase.input.port.ManageUserTasksUseCase;
import com.decathlon.domain_name.biz_ctx.infra.primary.rest.user.dtos.TaskDTO;
import com.decathlon.domain_name.biz_ctx.infra.primary.rest.user.mappers.TaskRestMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/tasks")
class UserTasksController {

    private final DisplayUsersUseCases displayUsersUseCases;
    private final ManageUserTasksUseCase manageUserTasksUseCase;
    private final TaskRestMapper taskRestMapper;

    @GetMapping
    @ResponseStatus(OK)
    public List<TaskDTO> getUserTasks(@PathVariable Long userId) {

        return displayUsersUseCases.getUserTasks(userId.intValue())
                .stream()
                .map(taskRestMapper::toDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public TaskDTO createTask(@PathVariable Integer userId, @RequestBody TaskDTO taskDTO) {

        return manageUserTasksUseCase.createTask(userId, taskRestMapper.toDomain(taskDTO))
                .map(taskRestMapper::toDTO)
                .orElseThrow();
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(OK)
    public TaskDTO getUserTask( @PathVariable Long userId, @PathVariable Integer taskId) {

        return taskRestMapper.toDTO(displayUsersUseCases.getUserTask(userId.intValue(), taskId));
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(NO_CONTENT)
    public void removeTask(@PathVariable Integer userId, @PathVariable Integer taskId) {

        manageUserTasksUseCase.removeTask(userId, taskId);
    }
}
