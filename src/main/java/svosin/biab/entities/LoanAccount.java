package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.persistEntities.PersistLoanAccount;

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
    Money latePaymentPenalty = Money.zero(Ruble.rub);

    Money initialAmount = Money.zero(Ruble.rub);
    Money repaidAmount = Money.zero(Ruble.rub);

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
        this.nextPaymentDueDate = this.openingDate.plusMonths(1);
        this.latePaymentPenalty = Money.of(Ruble.rub, 1000);
        this.interestRate = 5.0;
        this.initialAmount = Money.of(Ruble.rub, loanRequest.getRequestedSum());
    }

    public PersistLoanAccount toPersist() {
        return new PersistLoanAccount(id, interestRate, openingDate, expectedCloseDate, nextPaymentDueDate,
                latePaymentPenalty.toString(), initialAmount.toString(), repaidAmount.toString(), owner);
    }

    public LoanAccount(PersistLoanAccount p) {
        id = p.getId();
        interestRate = p.getInterestRate();
        openingDate = p.getOpeningDate();
        expectedCloseDate = p.getExpectedCloseDate();
        nextPaymentDueDate = p.getNextPaymentDueDate();
        if(p.getLatePaymentPenalty() != null)
        latePaymentPenalty = Money.parse(p.getLatePaymentPenalty());
        else latePaymentPenalty = Money.zero(Ruble.rub);
        if(p.getInitialAmount() != null)
        initialAmount = Money.parse(p.getInitialAmount());
        else initialAmount = Money.zero(Ruble.rub);
        if(p.getRepaidAmount() != null)
        repaidAmount = Money.parse(p.getRepaidAmount());
        else repaidAmount = Money.zero(Ruble.rub);
        owner = p.getOwner();
    }

}
