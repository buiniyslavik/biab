package svosin.biab.config;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import svosin.biab.jobs.LoansProcessInterest;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
public class JobConfiguration {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    private void initialize() throws Exception {
        schedulerFactoryBean.getScheduler().addJob(ProcessLoansDetail(), true, true);
        if (!schedulerFactoryBean.getScheduler().checkExists(new TriggerKey("trigger1", "mygroup"))) {
            schedulerFactoryBean.getScheduler().scheduleJob(simpleTriggerMyJobOne());
        }

    }

    private static JobDetail ProcessLoansDetail() {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setKey(new JobKey("jobone", "mygroup"));
        jobDetail.setJobClass(LoansProcessInterest.class);
        // remain stored in the job store even if no triggers point to it anymore
        jobDetail.setDurability(true);
        return jobDetail;
    }

    private static Trigger simpleTriggerMyJobOne() {
        return newTrigger()
                .forJob(ProcessLoansDetail())
                .withIdentity("trigger1", "mygroup")
                .withPriority(50)
                .withSchedule(cronSchedule("0 0/1 * 1/1 * ? *"))
                .build();

                // Job is scheduled for 3+1 times with the interval of 30 seconds
               /* .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(30)
                        .withRepeatCount(3))
                .startAt(DateBuilder.evenMinuteDateAfterNow())
                .build();*/
    }
}
