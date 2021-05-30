package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.enums.LogOperationType;

import java.util.Date;

@Data
@Document
public class PaymentLogEntry {
    @Id
    String id;
    String userId;
    String accountId;
    Date date;
    LogOperationType operationType;
    String providerName;
    String amount;
    String paymentData;

    public PaymentLogEntry(String userId, String accountId, LogOperationType operationType, String amount, String paymentData) {
        this.userId = userId;
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = amount;
        this.paymentData = paymentData;
    }
}
