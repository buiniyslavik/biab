package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDraftDTO {
    String provider;
    String accountToUseId;
    String accountToUseBalance;
    String amount;
    String fee;
    String total;
    String data;
}
