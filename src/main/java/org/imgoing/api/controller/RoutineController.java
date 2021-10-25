package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.mapper.RoutineMapper;
import org.imgoing.api.service.RoutineService;
import org.imgoing.api.service.RoutinetaskService;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "루틴 관련 API")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final TaskService taskService;
    private final RoutinetaskService routinetaskService;

    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping
    public ImgoingResponse<RoutineDto.Read> create(User user, @RequestBody RoutineDto.Create dto) {
        Routine newRoutine = routineService.create(routineMapper.toEntityForPost(user, dto));
        List<Task> tasks = taskService.getListById(dto.getTaskIdList());
        List<Routinetask> newRoutinetasks = routinetaskService.createAll(newRoutine.makeRoutinetasks(tasks));

        RoutineDto.Read result = routineMapper.toDto(newRoutine, newRoutinetasks);
        return new ImgoingResponse<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "루틴 전체 조회")
    @GetMapping
    public ImgoingResponse<List<RoutineDto.Read>> getAll(User user) {
        List<RoutineDto.Read> result = routineService.getListByUserId(user.getId()).stream()
                .map(routine -> routineMapper.toDto(routine, routine.getRoutinetasks()))
                .collect(Collectors.toList());
        return new ImgoingResponse<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "루틴 조회")
    @GetMapping("/{routineId}")
    public ImgoingResponse<RoutineDto.Read> get(
            User user,
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "routineId") Long id
    ) {
        Routine routine = routineService.getById(id);
        RoutineDto.Read result = routineMapper.toDto(routine, routine.getRoutinetasks());
        return new ImgoingResponse<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "루틴 삭제")
    @DeleteMapping("/{routineId}")
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "routineId") Long id
    ) {
        Routine routine = routineService.getById(id);
        String responseMessage = "루틴 " + routine.getName() + " 이(가) 삭제되었습니다.";
        routineService.delete(routine);

        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "루틴 수정")
    @PutMapping
    public ImgoingResponse<RoutineDto.Read> update(User user, @RequestBody RoutineDto.Update dto){
        routineService.update(routineMapper.toEntityForPut(user, dto));
        Routine routine = routineService.getById(dto.getId());
        List<Routinetask> routinetasks = routinetaskService.update(routine, dto.getTaskIdList());

        RoutineDto.Read result = routineMapper.toDto(routine, routinetasks);
        return new ImgoingResponse<>(result, HttpStatus.CREATED);
    }
}
