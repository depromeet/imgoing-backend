package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.SubtaskDto;
import org.imgoing.api.entity.Subtask;
import org.imgoing.api.mapper.SubtaskMapper;
import org.imgoing.api.service.SubtaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(value = "SubtaskController")
@RequestMapping("/api/v1/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;
    private final SubtaskMapper subtaskMapper;

    @ApiOperation(value = "세부업무 생성")
    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody SubtaskDto dto) {
        Subtask newSubtask = subtaskMapper.toEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subtaskService.create(newSubtask));
    }

    @ApiOperation(value = "세부업무 조회")
    @GetMapping("/{subtaskId}")
    public ResponseEntity<Object> get(@ApiParam(value = "세부업무 id", required = true, example = "1")
                                          @PathVariable(value = "subtaskId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(subtaskService.getById(id));
    }

    @ApiOperation(value = "세부업무 리스트 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getList(@ApiParam(value = "회원 id", required = true, example = "1")
                                          @PathVariable(value = "userId") Long userId){

        return ResponseEntity.status(HttpStatus.OK).body(
                subtaskService.getListByUserId(userId).stream()
                        .map(subtaskMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "세부업무 리스트 전체 조회")
    @GetMapping("/all")
    public ResponseEntity<Object> getListAll(){
        return ResponseEntity.status(HttpStatus.OK).body(
                subtaskService.getListAll().stream()
                        .map(subtaskMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "세부업무 삭제")
    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Object> delete(@ApiParam(value = "세부업무 id", required = true, example = "1")
                                         @PathVariable(value = "subtaskId") Long id){
        subtaskService.delete(subtaskService.getById(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("id = " + id + " 세부업무가 삭제되었습니다.");
    }

    @ApiOperation(value = "세부업무 수정")
    @PutMapping("/{subtaskId}")
    public ResponseEntity<Object> update(@RequestBody SubtaskDto dto){
        Subtask newSubtask = subtaskMapper.toEntity(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subtaskMapper.toDto(subtaskService.update(newSubtask)));
    }
}