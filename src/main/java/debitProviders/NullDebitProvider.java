package debitProviders;

import lombok.Data;
import org.joda.money.Money;
import svosin.biab.entities.Ruble;
import svosin.biab.interfaces.MoneyDebitProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class NullDebitProvider implements MoneyDebitProvider {

    @Override
    public Money getMinAmount() {
        return Money.of(Ruble.rub, 0.01);
    }

    @Override
    public Money getMaxAmount() {
        return Money.of(Ruble.rub, 1000000);
    }

    @Override
    public Money getFeeForAmount(Money amount) {
        return amount.multipliedBy(BigDecimal.valueOf(0.02), RoundingMode.UP);
    }

    @Override
    public Boolean processPayment(Money amount, String data) {
        return true;
    }
}
