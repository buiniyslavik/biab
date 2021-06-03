package svosin.biab.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import svosin.biab.repos.LoanAccountRepository;
import svosin.biab.services.LoansService;

import java.time.LocalDate;

@Slf4j
@DisallowConcurrentExecution
public class LoansProcessInterest extends QuartzJobBean {
    @Autowired
    LoansService loansService;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Processing interest...");
        /*
        TODO:
        fetch list of CAs for today and process interest
         */
        var accsForToday = loansService.getAllByDueDate(LocalDate.now().plusDays(1));
        accsForToday.forEach( acc -> {
            acc.processInterest();
        });
    }
}
