package org.imgoing.api.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;
import org.imgoing.api.dto.plan.PlanBookmarkDto;
import org.imgoing.api.dto.plan.PlanDto;
import org.imgoing.api.dto.plan.PlanRequest;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.dto.route.RemainingTimeResponse;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.service.PlanService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(tags = "약속 관련 API")
@RequestMapping("/api/v1/plans")
public class PlanController {
    private final PlanService planService;
    private final PlanMapper planMapper;

    @ApiOperation(value = "일정 생성")
    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "일정 생성 성공", response = PlanRequest.class),
            @ApiResponse(code = 400, message = "일정 생성 실패")
    })
    public ImgoingResponse<PlanDto> create(User user, @RequestBody @Valid PlanRequest.Create planSaveRequest) {
        Plan plan = planService.create(user, planSaveRequest);
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()), HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 전체 조회", notes = "사용자의 전체 일정 조회")
    @GetMapping
    @ApiResponse(code = 200, message = "일정 전체 조회 성공", response = List.class)
    public ImgoingResponse<List<PlanDto>> getAll(User user) {
        List<PlanDto> planResponses = planService.getAll(user).stream()
                .map(plan -> planMapper.toDto(plan, plan.getTaskList()))
                .collect(Collectors.toList());

        return new ImgoingResponse<>(planResponses, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 조회", notes = "사용자의 특정 일정 조회")
    @GetMapping("/{planId}")
    @ApiResponse(code = 200, message = "일정 조회 성공", response = PlanRequest.class)
    public ImgoingResponse<PlanDto> getOne(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ) {
        Plan plan = planService.getOne(user.getId(), planId);
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()));
    }

    @ApiOperation(value = "일정 수정")
    @PutMapping
    @ApiResponses({
            @ApiResponse(code = 200, message = "일정 수정 성공", response = PlanRequest.class),
            @ApiResponse(code = 400, message = "일정 수정 실패")
    })
    public ImgoingResponse<PlanDto> modify (User user, @RequestBody @Valid PlanRequest planRequest) {
        Plan plan = planService.modify(user.getId(), planRequest);
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()));
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{planId}")
    @ApiResponse(code = 204, message = "일정 삭제 성공", response = String.class)
    public ImgoingResponse<String> delete(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ) {
        planService.delete(user.getId(), planId);
        String responseMessage = "planId = " + planId + "일정이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "중요 일정 등록/삭제")
    @PostMapping("/bookmark/{planId}")
    @ApiResponse(code = 200, message = "중요 일정 등록/삭제 성공", response = PlanBookmarkDto.class)
    public ImgoingResponse<PlanBookmarkDto> registerImportant(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ){
        return new ImgoingResponse<>(planService.registerImportant(planId), HttpStatus.OK);
    }

    @ApiOperation(value = "중요 일정 조회")
    @GetMapping("/bookmark")
    @ApiResponse(code = 200, message = "중요 일정 조회 성공")
    public ImgoingResponse<List<PlanDto>> getImportantList(User user) {
        List<PlanDto> planResponses = planService.getImportantList(user.getId()).stream()
                .map(plan -> planMapper.toDto(plan, plan.getTaskList()))
                .collect(Collectors.toList());

        return new ImgoingResponse<>(planResponses, HttpStatus.OK);
    }

    @ApiOperation(value = "가장 최근 약속까지 남은 시간")
    @GetMapping("/remaining/time")
    public ImgoingResponse<RemainingTimeResponse> getRemainingTime (User user) {
        RemainingTimeInfoVo remainingTimeInfoVo = planService.getTimeRemainingUntilRecentPlan(user);
        return new ImgoingResponse<>(new RemainingTimeResponse(remainingTimeInfoVo));
    }
}
