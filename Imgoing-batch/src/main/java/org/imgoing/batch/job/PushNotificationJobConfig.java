package org.imgoing.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.repository.PlanRepository;
import org.imgoing.batch.publish.PublishMessage;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushNotificationJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RabbitTemplate rabbitTemplate;
    private final PlanRepository planRepository;

    private static final String EXCHANGE_NAME = "sample.exchange";

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .on("FAILED") // step1 결과가 FAILED일 경우
                .end() // 종료
                .from(step1()) // step1의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .end() // flow 종료
                .end() // job 종료
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("========step1 시작========");
                    // 1. 현재시간 구하기
                    LocalDateTime now = LocalDateTime.now();
                    log.info("1. now = {}", now);

                    // 2. 준비시작시간이 현재시간과 동일한 일정 가져오기
                    List<Plan> planList = planRepository.findAllByReadyStartAt(now);
                    log.info("2. planList size = {}", planList.size());

                    /*
                    // 2-1 시작될 일정이 없으면 return
                    if(planList.size() == 0) {
                        return RepeatStatus.FINISHED;
                    }

                    // 3. planList에서 message 만들기
                    List<PublishMessage> publishMessageList = planList.stream()
                            .map(plan -> PublishMessage.builder()
                                    .userId(plan.getUser().getId())
                                    .planId(plan.getId())
                                    .message("plan message")
                                    .build())
                            .collect(Collectors.toList());
                    log.info("3. publishMessageList size = {}", publishMessageList.size());
                    */

                    List<PublishMessage> publishMessageList = new ArrayList<>();
                    PublishMessage pm = PublishMessage.builder()
                            .userId(Long.parseLong("1"))
                            .planId(Long.parseLong("1"))
                            .message("test message")
                            .build();
                    publishMessageList.add(pm);
                    log.info("3. test publishMessageList size = {}", publishMessageList.size());

                    // 4. message publish 하기
                    try {
                        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "sample.#", "sample message");
                        log.info("4. publish 성공");
                    } catch (AmqpException e) {
                        log.info("publish error -> {}", e);
                        stepContribution.setExitStatus(ExitStatus.FAILED);
                    }

                    // 5. FINISHED 반환
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
