package io.springbatch.springbatchlectur;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Bean
//    @JobScope
    public Job job()
    {
        return jobBuilderFactory.get("Job")
                                .start(step1())
                                .next(step2())
                                .build();
    }
    @Bean
//    @StepScope
    public Step step1()
    {
        return stepBuilderFactory.get("step1")
                                 .tasklet(new Tasklet()
                                 {
                                     @Override
                                     public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                                     {
                                         JobParameters jobParameters = stepContribution.getStepExecution()
                                                                                       .getJobExecution()
                                                                                       .getJobParameters();

                                         jobParameters.getString("name");
                                         jobParameters.getLong("seq");
                                         jobParameters.getDate("date");
                                         jobParameters.getDouble("age");

                                         Map<String, Object> jobParameters1 = chunkContext.getStepContext()
                                                                                          .getJobParameters();

                                         log.info("step1 was executed");
                                         return RepeatStatus.FINISHED;
                                     }
                                 })
                                 .build();
    }
    @Bean
//    @StepScope
    public Step step2()
    {
        return stepBuilderFactory.get("step2")
                                 .tasklet(new Tasklet()
                                 {
                                     @Override
                                     public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                                     {
                                         log.info("step2 was executed");
                                         return RepeatStatus.FINISHED;
                                     }
                                 })
                                 .build();
    }
}
