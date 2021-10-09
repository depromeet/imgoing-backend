package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PresetDto;
import org.imgoing.api.domain.entity.Preset;
import org.imgoing.api.mapper.PresetMapper;
import org.imgoing.api.service.PresetService;
import org.imgoing.api.service.PlantaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "프리셋 API (v1에서 사용하지 않음)")
@RequestMapping("/api/v1/presets")
public class PresetController {
    private final PresetService presetService;
    private final PlantaskService plantaskService;
    private final PresetMapper presetMapper;

    @ApiOperation(value = "프리셋 생성")
    @PostMapping
    public ImgoingResponse<Preset> create(User user, @RequestBody PresetDto dto) {
        Preset newPreset = presetMapper.toEntity(dto);
        Preset savedPreset = presetService.create(newPreset);
        return new ImgoingResponse<>(savedPreset, HttpStatus.CREATED);
    }

    @ApiOperation(value = "프리셋 조회")
    @GetMapping("/{presetId}")
    public ImgoingResponse<Preset> get(
            User user,
            @ApiParam(value = "프리셋 id", required = true, example = "1")
            @PathVariable(value = "presetId") Long id
    ) {
        Preset foundPreset = presetService.getById(id);
        return new ImgoingResponse<>(foundPreset);
    }

//    @ApiOperation(value = "프리셋 리스트 유저별 조회")
//    @GetMapping("/{userId}")
//    public ResponseEntity<Object> getList(@ApiParam(value = "회원 id", required = true, example = "1")
//                                          @PathVariable(value = "userId") Long userId){
//        return ResponseEntity.status(HttpStatus.OK).body(
//                presetService.getListByUserId(userId).stream()
//                .map(presetMapper::toDto)
//                .collect(Collectors.toList())
//        );
//    }

    @ApiOperation(value = "프리셋 리스트 전체 조회")
    @GetMapping("/all")
    public ImgoingResponse<List<PresetDto>> getListAll(){
        List<PresetDto> presetDtoList = presetService.getListAll().stream()
                .map(presetMapper::toDto)
                .collect(Collectors.toList());
        return new ImgoingResponse<>(presetDtoList);
    }

    @ApiOperation(value = "프리셋 삭제")
    @DeleteMapping("/{presetId}")
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "프리셋 id", required = true, example = "1")
            @PathVariable(value = "presetId") Long id
    ) {
        presetService.delete(presetService.getById(id));
        String responseMessage = "id = " + id + " 프리셋이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "프리셋 정보 수정")
    @PutMapping
    public ImgoingResponse<PresetDto> update(User user, @RequestBody PresetDto dto){
        Preset newPreset = presetMapper.toEntity(dto);
        PresetDto updatedPresetDto = presetMapper.toDto(presetService.update(newPreset));
        return new ImgoingResponse<>(updatedPresetDto, HttpStatus.CREATED);
    }
}
