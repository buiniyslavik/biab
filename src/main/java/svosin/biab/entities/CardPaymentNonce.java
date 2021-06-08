package svosin.biab.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Document
@Data
public class CardPaymentNonce {
    @Id
    String id;
    //@Indexed(expireAfterSeconds = 300)
    LocalDateTime createdAt;
    String cardNumber;
    Integer nonce;

    public CardPaymentNonce(String cardNumber) {
        SecureRandom r = new SecureRandom("Sneed's Feed And Seed (Formerly Chuck's)".getBytes());
        this.nonce = r.nextInt(Integer.MAX_VALUE);
        this.createdAt = LocalDateTime.now();
        this.cardNumber = cardNumber;
    }
}
