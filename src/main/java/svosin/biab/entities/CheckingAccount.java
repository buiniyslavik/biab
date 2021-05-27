package svosin.biab.entities;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckingAccount {
    @Id
    String id;

    Money currentBalance;

    Profile owner;

    public CheckingAccount(Profile owner) {
        this.owner = owner;
        this.currentBalance = Money.zero(Ruble.rub);
    }
}
