package svosin.biab.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import svosin.biab.enums.LogOperationType;
import svosin.biab.services.LoansService;
import svosin.biab.services.PaymentLogService;

import java.time.LocalDate;

import static svosin.biab.config.QuartzConfiguration.CONTEXT_KEY;

@Slf4j
@Component
@DisallowConcurrentExecution
public class LoansProcessInterest extends QuartzJobBean {
    @Autowired
    LoansService loansService;

    @Autowired
    PaymentLogService paymentLogService;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Processing interest...");
        try {
            applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get(CONTEXT_KEY);
            loansService = applicationContext.getBean(LoansService.class);
            paymentLogService = applicationContext.getBean(PaymentLogService.class);
            log.info("looking for loans with due date " + LocalDate.now().minusDays(1).toString());
            var accsForToday = loansService.getAllByDueDate(LocalDate.now().minusDays(1));
            accsForToday.forEach(acc -> {
                acc.processInterest();
                loansService.save(acc);
                paymentLogService.logPayment(
                        acc.getOwner().getUserId(),
                        acc.getId(),
                        LogOperationType.DEBIT,
                        acc.getLastInterest().toString(),
                        "Начисление процентов");
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
