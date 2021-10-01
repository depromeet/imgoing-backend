package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.entity.Task;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.service.RoutineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(value = "TaskController")
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    private final RoutineService routineService;
    private final TaskMapper taskMapper;

    @ApiOperation(value = "업무 생성")
    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody TaskDto dto) {
        Task newTask = taskMapper.toEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.create(newTask));
    }

    @ApiOperation(value = "업무 조회")
    @GetMapping("/{taskId}")
    public ResponseEntity<Object> get(@ApiParam(value = "업무 id", required = true, example = "1")
                                      @PathVariable(value = "taskId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getById(id));
    }

//    @ApiOperation(value = "업무 리스트 유저별 조회")
//    @GetMapping("/{userId}")
//    public ResponseEntity<Object> getList(@ApiParam(value = "회원 id", required = true, example = "1")
//                                          @PathVariable(value = "userId") Long userId){
//        return ResponseEntity.status(HttpStatus.OK).body(
//                taskService.getListByUserId(userId).stream()
//                .map(taskMapper::toDto)
//                .collect(Collectors.toList())
//        );
//    }

    @ApiOperation(value = "업무 리스트 전체 조회")
    @GetMapping("/all")
    public ResponseEntity<Object> getListAll(){
        return ResponseEntity.status(HttpStatus.OK).body(
                taskService.getListAll().stream()
                        .map(taskMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "업무 삭제")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Object> delete(@ApiParam(value = "업무 id", required = true, example = "1")
                                                @PathVariable(value = "taskId") Long id){
        taskService.delete(taskService.getById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("id = " + id + " 업무이 삭제되었습니다.");
    }

    @ApiOperation(value = "업무 정보 수정")
    @PutMapping("/{taskId}")
    public ResponseEntity<Object> update(@RequestBody TaskDto dto){
        Task newTask =  taskMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskMapper.toDto(taskService.update(newTask)));
    }
}
