package svosin.biab.interfaces;

import org.joda.money.Money;
import svosin.biab.exceptions.PaymentProcessingException;

public interface MoneyDebitProvider {
    public String getName();
    public Money getMinAmount();
    public Money getMaxAmount();
    public String getFee();
    public Money getFeeForAmount(Money amount);
    public Boolean processPayment(Money amount, String data) throws PaymentProcessingException;
}
