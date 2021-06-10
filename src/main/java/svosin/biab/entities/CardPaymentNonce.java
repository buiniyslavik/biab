package svosin.biab.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Document
@Data
public class CardPaymentNonce {
    @Id
    String id;
    //@Indexed(expireAfterSeconds = 300)
    LocalDateTime createdAt;
    String cardNumber;
    String nonce;

    public CardPaymentNonce(String cardNumber) {
        SecureRandom r = new SecureRandom("Sneed's Feed And Seed (Formerly Chuck's)".getBytes());

        this.createdAt = LocalDateTime.now();
        this.nonce = this.createdAt.toString() + r.nextInt(Integer.MAX_VALUE);
        this.cardNumber = cardNumber;
    }
}
