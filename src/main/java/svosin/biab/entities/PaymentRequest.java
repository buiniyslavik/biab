package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import svosin.biab.interfaces.MoneyDebitProvider;

@Data
@NoArgsConstructor
public class PaymentRequest {
    String provider;
    String amount;
    String data;
}

