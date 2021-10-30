package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.routine.RoutineDto;
import org.imgoing.api.dto.routine.RoutineRequest;
import org.imgoing.api.mapper.RoutineMapper;
import org.imgoing.api.service.RoutineService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "루틴 관련 API")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping
    public ImgoingResponse<RoutineDto> create(User user, @RequestBody @Valid RoutineRequest routineRequest) {
        Routine newRoutine = routineService.create(
                routineMapper.toEntity(user, routineRequest),
                routineRequest.getTaskIdList()
        );
        RoutineDto response = routineMapper.toDto(newRoutine, newRoutine.getRoutinetasks());
        return new ImgoingResponse<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "루틴 전체 조회")
    @GetMapping
    public ImgoingResponse<List<RoutineDto>> getAll(User user) {
        List<RoutineDto> response = routineService.getListByUserId(user.getId()).stream()
                .map(routine -> routineMapper.toDto(routine, routine.getRoutinetasks()))
                .collect(Collectors.toList());
        return new ImgoingResponse<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "루틴 조회")
    @GetMapping("/{routineId}")
    public ImgoingResponse<RoutineDto> get(
            User user,
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "routineId") Long id
    ) {
        Routine routine = routineService.getById(id);
        RoutineDto response = routineMapper.toDto(routine, routine.getRoutinetasks());
        return new ImgoingResponse<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "루틴 삭제")
    @DeleteMapping("/{routineId}")
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "routineId") Long id
    ) {
        Routine routine = Routine.builder().id(id).build();
        String responseMessage = "루틴이 삭제되었습니다.";
        routineService.delete(routine);

        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "루틴 수정")
    @PutMapping("/{routineId}")
    public ImgoingResponse<RoutineDto> update(
            User user,
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "routineId") Long id,
            @RequestBody @Valid RoutineRequest routineRequest){
        Routine newRoutine = routineService.update(routineMapper.toEntity(id, user, routineRequest), routineRequest.getTaskIdList());
        RoutineDto response = routineMapper.toDto(newRoutine, newRoutine.getRoutinetasks());
        return new ImgoingResponse<>(response, HttpStatus.CREATED);
    }
}
