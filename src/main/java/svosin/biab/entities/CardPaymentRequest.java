package svosin.biab.entities;

import lombok.Data;

@Data
public class CardPaymentRequest {
    Integer nonce;
    String cardNumber;
    String amount;
    String merchantName;
}
