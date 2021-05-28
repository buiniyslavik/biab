package svosin.biab.creditProviders;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import svosin.biab.entities.Ruble;
import svosin.biab.interfaces.MoneyCreditProvider;

@Data
@NoArgsConstructor
public class NullCreditProvider implements MoneyCreditProvider {
    String name;
    Money minValue = Money.of(Ruble.rub, 1.0);
    Money maxValue = Money.of(Ruble.rub, 10000000.0);

    @Override
    public Boolean processCredit(Money amount) {
        return true;
    }
}
