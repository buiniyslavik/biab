package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PublicKey;

@Document
@Data
@NoArgsConstructor
public class CardKey {
    @Id
    String id;

    public CardKey(PublicKey publicKey, String card) {
        this.publicKey = publicKey;
        this.card = card;
    }

    PublicKey publicKey;

    String card;
}
