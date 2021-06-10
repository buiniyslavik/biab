package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {
    String accountToUseId;
    String provider;
    String amount;
    String data;
}

