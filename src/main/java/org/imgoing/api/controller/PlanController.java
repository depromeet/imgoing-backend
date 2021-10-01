package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.entity.Plan;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.service.PlanService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Api(value = "PlanController")
@RequestMapping("/api/v1/plan")
public class PlanController {
    private final PlanService planService;
    private final PlanMapper planMapper;

    @ApiOperation(value = "일정 생성")
    @PostMapping("")
    public ImgoingResponse<Plan> create(@RequestBody PlanDto planDto) {
        Plan newPlan = planMapper.toEntity(planDto);
        Plan savedPlan = planService.create(newPlan);
        return new ImgoingResponse<>(savedPlan, HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 조회")
    @GetMapping("/{userId}")
    public ImgoingResponse<List<PlanDto>> getList(
            @ApiParam(value = "회원 id", required = true, example = "1")
            @PathVariable(value = "userId") Long userId
    ) {
        List<PlanDto> planDtoList = planService.getPlanByUserId(userId).stream()
                .map(planMapper::toDto)
                .collect(Collectors.toList());
        return new ImgoingResponse<>(planDtoList);
    }

    @ApiOperation(value = "일정 삭제")
    @DeleteMapping("/{planId}")
    public ImgoingResponse<String> delete(
            @ApiParam(value = "루틴 id", required = true, example = "1")
            @PathVariable(value = "planId") Long id
    ) {
        planService.delete(planService.getPlanById(id));
        String responseMessage = "id = " + id + "일정이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }
}
