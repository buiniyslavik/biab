package svosin.biab.entities;

import lombok.Data;

@Data
public class CardPaymentRequest {
    String cardNumber;
    String amount;
    String merchantName;
}
