package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.task.TaskRequest;
import org.imgoing.api.dto.task.TaskResponse;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "준비항목 관련 API")
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @ApiOperation(value = "준비항목 생성")
    @PostMapping
    public ImgoingResponse<TaskResponse> create(
            User user,
            @RequestBody @Valid TaskRequest taskRequest
    ) {
        Task newTask = taskService.create(taskMapper.requestToEntity(user, taskRequest));
        return new ImgoingResponse<>(taskMapper.toDto(newTask), HttpStatus.CREATED);
    }

    @ApiOperation(value = "준비항목 조회")
    @GetMapping("/{taskId}")
    public ImgoingResponse<TaskResponse> get(
            User user,
            @ApiParam(value = "준비항목 id", required = true, example = "1")
            @PathVariable(value = "taskId") Long id
    ) {
        Task foundTask = taskService.getById(id);
        return new ImgoingResponse<>(taskMapper.toDto(foundTask));
    }

    @ApiOperation(value = "준비항목 리스트 전체 조회")
    @GetMapping
    public ImgoingResponse<List<TaskResponse>> getListAll(User user){
        List<TaskResponse> responses = taskService.getListByUserId(user.getId()).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
        return new ImgoingResponse<>(responses);
    }

    @ApiOperation(value = "준비항목 삭제")
    @DeleteMapping("/{taskId}")
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "준비항목 id", required = true, example = "1")
            @PathVariable(value = "taskId") Long id
    ) {
        Task task = taskService.getById(id);
        String name = task.getName();
        taskService.delete(task);
        String responseMessage = "준비항목 " + name + " 이(가) 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "준비항목 수정")
    @PutMapping("/{taskId}")
    public ImgoingResponse<TaskResponse> update (
            User user,
            @ApiParam(value = "준비항목 id", required = true, example = "1")
            @PathVariable(value = "taskId") Long id,
            @RequestBody @Valid TaskRequest taskRequest
    ) {
        Task willUpdateTask = taskMapper.requestToEntity(id, user, taskRequest);
        TaskResponse response = taskMapper.toDto(taskService.update(willUpdateTask));
        return new ImgoingResponse<>(response, HttpStatus.CREATED);
    }
}