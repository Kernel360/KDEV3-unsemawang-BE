package com.palbang.unsemawang.common.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final Job memberMatchingScoreJob;

	@Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
	public void runMatchingScoreBatch() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis()) // 중복 실행을 방지하기 위한 고유 값 추가
				.toJobParameters();

			JobExecution jobExecution = jobLauncher.run(memberMatchingScoreJob, jobParameters);
			System.out.println("배치 실행 완료: " + jobExecution.getStatus());

		} catch (Exception e) {
			System.err.println("배치 실행 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
}