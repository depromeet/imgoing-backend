package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.mapper.RoutineMapper;
import org.imgoing.api.service.RoutineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(value = "RoutineController")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody RoutineDto dto) {
        Routine newRoutine =  routineMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineService.create(newRoutine));
    }

    @ApiOperation(value = "투린 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getList(@ApiParam(value = "회원 id", required = true, example = "1")
                                          @PathVariable(value = "userId") Long userId){

        return ResponseEntity.status(HttpStatus.OK).body(
                routineService.getListByUserId(userId).stream()
                .map(routineMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "루틴 삭제")
    @DeleteMapping("/{routineId}")
    public ResponseEntity<Object> delete(@ApiParam(value = "루틴 id", required = true, example = "1")
                                                @PathVariable(value = "routineId") Long id){
        routineService.delete(routineService.getById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("id = " + id + " 루틴이 삭제되었습니다.");
    }

    @ApiOperation(value = "루틴 정보 수정")
    @PutMapping("/{routineId}")
    public ResponseEntity<Object> update(@RequestBody RoutineDto dto){
        Routine newRoutine =  routineMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineMapper.toDto(routineService.update(newRoutine)));
    }
}
