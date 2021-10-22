package org.imgoing.api.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.mapper.PlanMapper;
import org.imgoing.api.mapper.TaskMapper;
import org.imgoing.api.service.PlanService;
import org.imgoing.api.service.PlantaskService;
import org.imgoing.api.service.TaskService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = "약속 관련 API")
@RequestMapping("/api/v1/plans")
public class PlanController {
    private final PlanService planService;
    private final TaskService taskService;
    private final PlantaskService plantaskService;

    private final PlanMapper planMapper;
    private final TaskMapper taskMapper;

    @ApiOperation(value = "일정 생성")
    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "일정 생성 성공", response = PlanDto.class),
            @ApiResponse(code = 400, message = "일정 생성 실패")
    })
    public ImgoingResponse<PlanDto> create(User user, @RequestBody @Valid PlanDto planDto) {
        Plan plan = planMapper.toEntity(planDto);
        plan.addUser(user);
        plan = planService.createPlan(plan);

        List<TaskDto> taskDtos = planDto.getTask();
        List<Task> tasks = new ArrayList<>();

        // 준비항목이 없는 경우
        if(taskDtos.size() == 0) {
            return new ImgoingResponse<>(planMapper.toDto(plan, tasks), HttpStatus.CREATED);
        }
        
        // 준비항목이 있는 경우
        for(TaskDto taskDto : taskDtos) {
            if (!taskDto.getIsBookmarked()) {
                // 새로운 task는 db에 저장
                tasks.add(taskService.create(taskMapper.toEntity(taskDto)));
            } else {
                tasks.add(taskMapper.toEntity(taskDto));
            }
        }
        
        // plantask 저장
        plan.setPlantask(tasks);
        plantaskService.saveAll(plan.getPlantasks());
        
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()), HttpStatus.CREATED);
    }

    @ApiOperation(value = "일정 전체 조회", notes = "사용자의 전체 일정 조회")
    @GetMapping
    @ApiResponse(code = 200, message = "일정 전체 조회 성공", response = List.class)
    public ImgoingResponse<List<PlanDto>> getAllPlans(User user) {
        List<Plan> plans = planService.getPlans(user.getId());

        List<PlanDto> planDtos = new ArrayList<>();
        for(Plan plan : plans) {
            planDtos.add(planMapper.toDto(plan, plan.getTaskList()));
        }

        return new ImgoingResponse<>(planDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "일정 조회", notes = "사용자의 특정 일정 조회")
    @GetMapping("/{planId}")
    @ApiResponse(code = 200, message = "일정 조회 성공", response = PlanDto.class)
    public ImgoingResponse<PlanDto> getPlan(
            User user,
            @ApiParam(value = "일정 id", required = true, example = "1")
            @PathVariable(value = "planId") Long planId
    ) {
        Plan plan = planService.getPlan(user.getId(), planId);
        return new ImgoingResponse<>(planMapper.toDto(plan, plan.getTaskList()));
    }

    @ApiOperation(value = "일정 수정")
    @PutMapping
    @ApiResponses({
            @ApiResponse(code = 200, message = "일정 수정 성공", response = PlanDto.class),
            @ApiResponse(code = 400, message = "일정 수정 실패")
    })
    public ImgoingResponse<PlanDto> update (User user, @RequestBody @Valid PlanDto planDto) {
        // plan update
        Plan plan = planService.updatePlan(user.getId(), planDto);

        List<TaskDto> taskDtos = planDto.getTask();

        // 기존 plantask 삭제
        plantaskService.deleteByPlanId(plan.getId());
        // 기존 task 삭제
        planService.deleteTask(plan);

        List<Task> tasks = new ArrayList<>();

        // 준비항목이 없는 경우
        if(taskDtos.size() == 0) {
            return new ImgoingResponse<>(planMapper.toDto(plan, tasks), HttpStatus.CREATED);
        }

        // 준비항목이 있는 경우
        for(TaskDto taskDto : taskDtos) {
            if (!taskDto.getIsBookmarked()) {
                // 새로운 task는 db에 저장
                tasks.add(taskService.create(taskMapper.toEntity(taskDto)));
            } else {
                tasks.add(taskMapper.toEntity(taskDto));
            }
        }

        // plantask 저장
        plan.setPlantask(tasks);
        plantaskService.saveAll(plan.getPlantasks());

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
        planService.deletePlan(user.getId(), planId);
        String responseMessage = "planId = " + planId + "일정이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }
}