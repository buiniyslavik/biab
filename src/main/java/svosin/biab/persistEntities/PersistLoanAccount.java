package svosin.biab.persistEntities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.entities.Profile;

import java.time.LocalDate;

@Document
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistLoanAccount {
    @Id
    String id;

    Double interestRate;
    LocalDate openingDate;
    LocalDate expectedCloseDate;
    LocalDate nextPaymentDueDate;
    String latePaymentPenalty;

    String initialAmount;
    String repaidAmount;

    Profile owner;
}
