package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.entity.Subtask;
import org.imgoing.api.entity.Task;
import org.imgoing.api.mapper.RoutineMapper;
import org.imgoing.api.mapper.SubtaskMapper;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.service.RoutineService;
import org.imgoing.api.service.SubtaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(value = "RoutineController")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;
    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RoutineDto.Create dto) {
        Task task = Task.builder().id(dto.getTaskId()).build();

        List<Subtask> subtaskList = dto.getSubtaskIdList()
                .stream()
                .map(subtaskId -> Subtask.builder().id(subtaskId).build())
                .collect(Collectors.toList());

//        Task task = taskService.getById(dto.getTaskId());
//
//        List<Subtask> subtaskList = dto.getSubtaskIdList()
//                .stream()
//                .map(subtaskService::getById)
//                .collect(Collectors.toList());

        Routine newRoutine = routineMapper.toEntityForPost(task, subtaskList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineMapper.toDto(routineService.create(newRoutine)));
    }
}
