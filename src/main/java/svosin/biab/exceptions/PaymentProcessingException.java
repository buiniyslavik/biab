package svosin.biab.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentProcessingException extends Exception {
    public PaymentProcessingException(String msg) {
        super(msg);
    }
}
