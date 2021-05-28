package svosin.biab.interfaces;

import org.joda.money.Money;

public interface MoneyCreditProvider {
    public String getName();
    public Money getMinValue();
    public Money getMaxValue();
    public Boolean processCredit(Money amount);
}
