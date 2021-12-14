package org.imgoing.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgoing.batch.job.PushNotificationJobConfig;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final JobLauncher jobLauncher;
    private final PushNotificationJobConfig pushNotificationJobConfig;

    @Scheduled(cron = "0 * * * * *")
    public void runJob() {
        Map<String, JobParameter> configMap = new HashMap<>();
        configMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(configMap);

        try {
            jobLauncher.run(pushNotificationJobConfig.job(), jobParameters);
        }  catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error(e.getMessage());
        }
    }
}
