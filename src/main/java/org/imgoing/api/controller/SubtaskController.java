package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.SubtaskDto;
import org.imgoing.api.entity.Subtask;
import org.imgoing.api.mapper.SubtaskMapper;
import org.imgoing.api.service.SubtaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "준비항목 관련 API")
@RequestMapping("/api/v1/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;
    private final SubtaskMapper subtaskMapper;

    @ApiOperation(value = "준비항목 생성")
    @PostMapping
    public ImgoingResponse<SubtaskDto> create(@RequestBody SubtaskDto dto) {
        Subtask newSubtask = subtaskMapper.toEntity(dto);
        Subtask savedSubtask = subtaskService.create(newSubtask);
        return new ImgoingResponse<>(subtaskMapper.toDto(savedSubtask), HttpStatus.CREATED);
    }

    @ApiOperation(value = "준비항목 조회")
    @GetMapping("/{subtaskId}")
    public ImgoingResponse<SubtaskDto> get(
            @ApiParam(value = "준비항목 id", required = true, example = "1")
            @PathVariable(value = "subtaskId") Long id
    ) {
        Subtask foundSubtask = subtaskService.getById(id);
        return new ImgoingResponse<>(subtaskMapper.toDto(foundSubtask));
    }

//    @ApiOperation(value = "준비항목 리스트 조회")
//    @GetMapping("/{userId}")
//    public ImgoingResponse<List<SubtaskDto>> getList(
//            @ApiParam(value = "회원 id", required = true, example = "1")
//            @PathVariable(value = "userId") Long userId
//    ) {
//        List<SubtaskDto> subtaskDtoList = subtaskService.getListByUserId(userId).stream()
//                .map(subtaskMapper::toDto)
//                .collect(Collectors.toList());
//        return new ImgoingResponse<>(subtaskDtoList);
//    }

    @ApiOperation(value = "준비항목 리스트 전체 조회")
    @GetMapping("/all")
    public ImgoingResponse<List<SubtaskDto>> getListAll(){
        List<SubtaskDto> subtaskDtoList = subtaskService.getListAll().stream()
                .map(subtaskMapper::toDto)
                .collect(Collectors.toList());
        return new ImgoingResponse<>(subtaskDtoList);
    }

    @ApiOperation(value = "준비항목 삭제")
    @DeleteMapping("/{subtaskId}")
    public ImgoingResponse<String> delete(
            @ApiParam(value = "준비항목 id", required = true, example = "1")
            @PathVariable(value = "subtaskId") Long id
    ) {
        Subtask subtask = subtaskService.getById(id);
        String name = subtask.getName();
        subtaskService.delete(subtask);
        String responseMessage = "준비항목" + name + "이(가) 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "준비항목 수정")
    @PutMapping
    public ImgoingResponse<SubtaskDto> update (@RequestBody SubtaskDto dto){
        Subtask willUpdateSubtask = subtaskMapper.toEntity(dto);
        SubtaskDto subtaskDto = subtaskMapper.toDto(subtaskService.update(willUpdateSubtask));
        return new ImgoingResponse<>(subtaskDto);
    }
}