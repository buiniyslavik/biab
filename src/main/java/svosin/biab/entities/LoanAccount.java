package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Data
@Document
public class LoanAccount {
    @Id
    String id;

    Double interestRate;
    Date openingDate;
    Date expectedCloseDate;
    Date nextPaymentDueDate;
    Money latePaymentPenalty;

    Money initialAmount;
    Money repaidAmount;

    @DBRef
    Profile owner;
    public LoanAccount(Double interestRate, Date expectedCloseDate, Money latePaymentPenalty, Money initialAmount) {
        this.interestRate = interestRate;
        this.openingDate = Calendar.getInstance().getTime();
        this.expectedCloseDate = expectedCloseDate;
        this.latePaymentPenalty = latePaymentPenalty;
        this.initialAmount = initialAmount;
    }
}
