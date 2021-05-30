package svosin.biab.interfaces;

import org.joda.money.Money;
import svosin.biab.exceptions.PaymentProcessingException;

public interface MoneyDebitProvider {
    public Money getMinAmount();
    public Money getMaxAmount();
    public Money getFeeForAmount(Money amount);
    public Boolean processPayment(Money amount, String data) throws PaymentProcessingException;
}