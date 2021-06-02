package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Data
@Document
public class LoanAccount {
    @Id
    String id;

    Double interestRate;
    LocalDate openingDate;
    LocalDate expectedCloseDate;
    LocalDate nextPaymentDueDate;
    Money latePaymentPenalty;

    Money initialAmount;
    Money repaidAmount;

    @DBRef
    Profile owner;
    public LoanAccount(Double interestRate, LocalDate expectedCloseDate, Money latePaymentPenalty, Money initialAmount) {
        this.interestRate = interestRate;
        this.openingDate = LocalDate.now();
        this.expectedCloseDate = expectedCloseDate;
        this.latePaymentPenalty = latePaymentPenalty;
        this.initialAmount = initialAmount;
    }

    public LoanAccount(LoanRequest loanRequest) {
        this.openingDate = LocalDate.now();
        this.expectedCloseDate = this.openingDate.plusMonths(loanRequest.getRequestedTerm());
        this.latePaymentPenalty = Money.of(Ruble.rub, 1000);
        this.interestRate = 5.0;
        this.initialAmount = Money.of(Ruble.rub, loanRequest.getRequestedSum());
    }

}
