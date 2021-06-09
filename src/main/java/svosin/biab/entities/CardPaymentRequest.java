package svosin.biab.entities;

import lombok.Data;

@Data
public class CardPaymentRequest {
    String nonce;
    String cardNumber;
    String amount;
    String merchantName;
}
