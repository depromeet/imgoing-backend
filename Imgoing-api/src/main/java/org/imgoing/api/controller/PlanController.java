package org.imgoing.api.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.domain.vo.RemainingTimeInfoVo;
import org.imgoing.api.dto.plan.ImportantPlanDto;
import org.imgoing.api.dto.plan.PlanArrivalRequest;
import org.imgoing.api.dto.plan.PlanDto;
import org.imgoing.api.dto.plan.PlanRequest;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.dto.route.RemainingTimeResponse;
import org.imgoing.api.dto.user.UserMonthlyStatResponse;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.service.PlanService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
        Plan newPlan = planMapper.toEntity(user, planSaveRequest);
        Plan plan = planService.create(newPlan, planSaveRequest.getTask(), planSaveRequest.getBookmarkedTaskIds());
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
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ) {
        Plan plan = (Plan)httpServletRequest.getAttribute("plan");
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()));
    }

    @ApiOperation(value = "일정 조회", notes = "최근 7일 일정 조회")
    @GetMapping("")
    @ApiResponse(code = 200, message = "일정 조회 성공", response = PlanRequest.class)
    public ImgoingResponse<List<PlanDto>> getHistoryDaysAgo(
            User user,
            @RequestParam("days") Integer days
    ) {
        List<PlanDto> planHistory = this.planService.getPlanHistoryDaysAgo(user, days).stream()
                .map(plan -> planMapper.toDto(plan, plan.getTaskList()))
                .collect(Collectors.toList());
        return new ImgoingResponse<>(planHistory, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 수정")
    @PutMapping("/{planId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "일정 수정 성공", response = PlanRequest.class),
            @ApiResponse(code = 400, message = "일정 수정 실패")
    })
    public ImgoingResponse<PlanDto> modify (
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId,
            @RequestBody @Valid PlanRequest planRequest
    ) {
        Plan oldPlan = (Plan)httpServletRequest.getAttribute("plan");
        Plan newPlan = planMapper.toEntity(planRequest);
        Plan modifiedplan = planService.modify(oldPlan, newPlan, planRequest.getTask());
        return new ImgoingResponse<>(planMapper.toDto(modifiedplan, modifiedplan.getTaskList()));
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{planId}")
    @ApiResponse(code = 204, message = "일정 삭제 성공", response = String.class)
    public ImgoingResponse<String> delete(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ) {
        planService.delete((Plan)httpServletRequest.getAttribute("plan"));
        String responseMessage = "planId = " + planId + "일정이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }

    @ApiOperation(value = "중요 일정 등록/삭제")
    @PostMapping("/important/{planId}")
    @ApiResponse(code = 200, message = "중요 일정 등록/삭제 성공", response = ImportantPlanDto.class)
    public ImgoingResponse<ImportantPlanDto> registerImportant(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ){
        Plan plan = (Plan)httpServletRequest.getAttribute("plan");
        return new ImgoingResponse<>(planService.registerImportant(plan.getId()), HttpStatus.OK);
    }

    @ApiOperation(value = "중요 일정 조회")
    @GetMapping("/important")
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

    @ApiOperation(value = "일정 마무리 정보 기록")
    @PostMapping("/arrival/{planId}")
    public ImgoingResponse<Map<String, Boolean>> getMonthlyStat (
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId,
            @RequestBody @Valid PlanArrivalRequest planArrivalRequest
    ) {
        Plan plan = (Plan)httpServletRequest.getAttribute("plan");
        planService.recordArrivalInformation(plan, planArrivalRequest);
        return new ImgoingResponse<>(Map.of("isSuccess", true));
    }
}
