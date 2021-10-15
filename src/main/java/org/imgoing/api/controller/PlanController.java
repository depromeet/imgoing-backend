package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.service.PlanService;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "약속 관련 API")
@RequestMapping("/api/v1/plan")
public class PlanController {
    private final PlanService planService;
    private final TaskService taskService;
    private final PlanMapper planMapper;

    @ApiOperation(value = "일정 생성")
    @PostMapping
    @ApiResponse(code = 201, message = "일정 생성 성공", response = PlanDto.class)
    public ImgoingResponse<PlanDto> create(User user, @RequestBody PlanDto planDto) {
        Plan plan = planMapper.toEntity(planDto);
        Plan savedPlan = planService.create(user, plan);
        // Task service와 연결
        // PlanTask 연결
        return new ImgoingResponse<>(planMapper.toDto(savedPlan), HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 전체 조회", notes = "사용자의 전체 일정 조회")
    @GetMapping
    @ApiResponse(code = 200, message = "일정 전체 조회 성공", response = List.class)
    public ImgoingResponse<List<PlanDto>> getAllPlans(User user) {
        List<PlanDto> planDtoList = planService.getPlanByUserId(user.getId()).stream()
                .map(planMapper::toDto)
                .collect(Collectors.toList());
        return new ImgoingResponse<>(planDtoList);
    }

    @ApiOperation(value = "일정 조회", notes = "사용자의 특정 일정 조회")
    @GetMapping("/{planId}")
    @ApiResponse(code = 200, message = "일정 조회 성공", response = PlanDto.class)
    public ImgoingResponse<PlanDto> getPlan(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long id
    ) {
        Plan plan = planService.getPlanById(id);
        return new ImgoingResponse<>(planMapper.toDto(plan));
    }

    @ApiOperation(value = "일정 수정")
    @PutMapping
    @ApiResponse(code = 200, message = "일정 수정 성공", response = PlanDto.class)
    public ImgoingResponse<PlanDto> update (User user, @RequestBody PlanDto planDto) {
        return new ImgoingResponse<>(planMapper.toDto(planService.update(planDto)));
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{planId}")
    @ApiResponse(code = 204, message = "일정 삭제 성공", response = String.class)
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long id
    ) {
        planService.delete(planService.getPlanById(id));
        String responseMessage = "id = " + id + "일정이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }
}
