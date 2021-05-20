package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class SavingsAccount {
    @Id
    String id;

    Money initialAmount;
    Money currentAmount;

    Date openingDate;
    Date expirationDate;

    Double interestRate;

    Profile owner;
}
