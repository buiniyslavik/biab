package svosin.biab.debitProviders;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import svosin.biab.entities.Ruble;
import svosin.biab.interfaces.MoneyDebitProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Slf4j
public class NullDebitProvider implements MoneyDebitProvider {
    @Override
    public String getName() {
        return "Тестовый платёж";
    }

    @Override
    public String getFee() {
        return "2%";
    }

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
        log.info("Paid to: "+ data + ", amount " + amount.toString());
        return true;
    }
}
