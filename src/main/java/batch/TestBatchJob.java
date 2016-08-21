package batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class TestBatchJob {

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job testJob() {

        // メディアのリスト取得
        List<String> exeList = new ArrayList<>();


        Step step1 = steps.get("step1")
                .tasklet((contribution, chunkContext) -> {
//                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step2 = steps.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
        Step step3 = steps.get("step3")
                .tasklet((contribution, chunkContext) -> {
//                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step4 = steps.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step5 = steps.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step6 = steps.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    Thread.sleep(1000);
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step7 = steps.get("step7")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step8 = steps.get("step8")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

        Step step9 = steps.get("step9")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

        Flow step1Flow = new FlowBuilder<SimpleFlow>("flow1")
                .start(step1)
                .next(step2)
                .on(ExitStatus.FAILED.getExitCode())
                .end()
                .next(step3)
                .build();

        Flow step2Flow = new FlowBuilder<SimpleFlow>("flow2")
                .start(step4)
                .next(step5)
                .build();

        Flow step3Flow = new FlowBuilder<SimpleFlow>("flow3")
                .split(new SimpleAsyncTaskExecutor())
                .add(
                        new FlowBuilder<SimpleFlow>("flow3-split1").start(step6).next((step7)).build(),
                        new FlowBuilder<SimpleFlow>("flow3-split2").start(step8).build())
                .build();

        return jobs
                .get("testJob2")
                .start(step1Flow)
                .next(step2Flow)
                .next(step3Flow)
                .next(step9)
                .end()
                .build();
    }
}
