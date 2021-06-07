package svosin.biab.entities;

import lombok.Data;

@Data
public class SignedCardPaymentRequest {
    CardPaymentRequest request;
    String signature;
}
