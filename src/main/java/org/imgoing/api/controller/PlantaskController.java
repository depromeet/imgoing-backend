package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PlantaskDto;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.mapper.PlantaskMapper;
import org.imgoing.api.service.PlantaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "구성된 준비항목 관련 API")
@RequestMapping("/api/v1/plantasks")
public class PlantaskController {
    private final PlantaskService plantaskService;
    private final PlantaskMapper plantaskMapper;

    @ApiOperation(value = "구성된 준비항목 생성")
    @PostMapping
    public ImgoingResponse<PlantaskDto.Read> create(User user, @RequestBody PlantaskDto.Create dto) {
        List<Task> taskList = dto.getTaskIdList()
                .stream()
                .map(taskId -> Task.builder().id(taskId).build())
                .collect(Collectors.toList());

        Plantask newPlantask = plantaskMapper.toEntityForPost(0L, taskList);

        return new ImgoingResponse<>(plantaskMapper.toDto(plantaskService.create(newPlantask)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "구성된 준비항목 조회")
    @GetMapping("/{plantaskId}")
    public ImgoingResponse<PlantaskDto.Read> getById(
            User user,
            @ApiParam(value = "구성된 준비항목 id", required = true, example = "1")
            @PathVariable(value = "plantaskId") Long id
    ) {
        Plantask plantask = plantaskService.getById(id);

        return new ImgoingResponse<>(plantaskMapper.toDto(plantask));
    }

    @ApiOperation(value = "구성된 준비항목 리스트 전체 조회")
    @GetMapping("/all")
    public ImgoingResponse<List<PlantaskDto.Read>> getListAll() {
        List<PlantaskDto.Read> plantaskDtoList = plantaskService.getListAll().stream()
                .map(plantaskMapper::toDto)
                .collect(Collectors.toList());

        return new ImgoingResponse<>(plantaskDtoList);
    }

    @ApiOperation(value = "구성된 준비항목 수정")
    @PutMapping("/{plantaskId}")
    public ImgoingResponse<PlantaskDto.Read> update(
            User user,
            @RequestBody PlantaskDto.Read plantaskDto,
            @ApiParam(value = "구성된 준비항목 id", required = true, example = "1")
            @PathVariable(value = "plantaskId") Long id
    ) {
        Plantask newPlantask = plantaskMapper.toEntityForPut(plantaskDto, id);

        return new ImgoingResponse<>(plantaskMapper.toDto(plantaskService.update(newPlantask)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "구성된 준비항목 삭제")
    @DeleteMapping("/{plantaskId}")
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "구성된 준비항목 id", required = true, example = "1")
            @PathVariable(value = "plantaskId") Long id
    ) {
        Plantask plantask = plantaskService.getById(id);

        plantaskService.delete(plantask);
        String responseMessage = "id = " + id + "구성된 준비항목이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }
}