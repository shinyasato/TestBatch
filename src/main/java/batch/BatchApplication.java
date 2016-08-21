package batch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(value = "/batch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BatchApplication {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job testJob;

    @RequestMapping(value = "/launch")
    public String launch() {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("run.date", Date.from(LocalDateTime.of(2016, 8, 10, 00, 00, 00).atZone(ZoneId.systemDefault()).toInstant()));
//        jobParametersBuilder.addDate("run.date", new Date());

        try {
            jobLauncher.run(testJob, jobParametersBuilder.toJobParameters());

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
            return "error";
        } catch (JobRestartException e) {
            e.printStackTrace();
            return "error";
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
            return "error";
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
