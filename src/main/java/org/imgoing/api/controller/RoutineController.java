package org.imgoing.api.controller;

import com.sun.istack.NotNull;
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

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(value = "RoutineController")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping("")
    public ResponseEntity<Object> createRoutine(@RequestBody RoutineDto dto) {
        Routine newRoutine =  routineMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineService.create(newRoutine));
    }

    @ApiOperation(value = "루틴 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRoutine(@ApiParam(value = "루틴 id", required = true, example = "1")
                                                @PathVariable(value = "id") Long id){
        routineService.delete(routineService.getById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("id = " + id + " 루틴이 삭제되었습니다.");
    }

    @ApiOperation(value = "루틴 정보 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRoutine(@RequestBody RoutineDto dto,
                                                @ApiParam(value = "루틴 id", required = true, example = "1")
                                                @PathVariable(value = "id") Long id){
        Routine newRoutine =  routineMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineMapper.toDto(routineService.update(newRoutine)));
    }
}
