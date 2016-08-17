package batch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        return null;
                    }
                })
                .build();
    }

    @Bean
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job1")
//                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    public static void main(String [] args) {
            System.exit(SpringApplication.exit(SpringApplication.run(BatchConfiguration.class, args)));
    }

    @Override
    public void run(String... args) {

        System.out.println("start...");
        Arrays.stream(args).forEach(System.out::println);

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addDate("run.date", Date.from(LocalDateTime.of(2016, 8, 1, 00, 00, 00).atZone(ZoneId.systemDefault()).toInstant()));

        try {
            jobLauncher.run(job(step1()), builder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (JobRestartException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        System.out.println("end...");
    }

}
