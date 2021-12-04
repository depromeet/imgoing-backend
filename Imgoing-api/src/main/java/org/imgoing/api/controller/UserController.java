package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.task.TaskDto;
import org.imgoing.api.dto.user.UserMonthlyStatResponse;
import org.imgoing.api.service.UserService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@Api(tags = "유저 관련 API")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "유저 이달 현황 조회")
    @GetMapping("/stats/month")
    public ImgoingResponse<UserMonthlyStatResponse> getMonthlyStat (User user) {
        long[] userMonthlyStat = userService.getMonthlyUserStats(user);
        long totalPlanCount = userMonthlyStat[0];
        long latePlanCount = userMonthlyStat[1];
        UserMonthlyStatResponse userMonthlyStatResponse = new UserMonthlyStatResponse(totalPlanCount, latePlanCount);
        return new ImgoingResponse<>(userMonthlyStatResponse);
    }
}
