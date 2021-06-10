package svosin.biab.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Random;

@Data
@Document
public class Card {
    @Id
    String id;
    @Field("cardNumber")
    String cardNumber;


    @Field("associatedAccount")
    String associatedAccount;

    @Field("associatedProfile")
    String associatedProfile;

    @Field
    String publicKey;

    public Card(Card card, String publicKey) {
        this.id = card.id;
        this.cardNumber = card.cardNumber;
        this.associatedAccount = card.associatedAccount;
        this.associatedProfile = card.associatedProfile;
        this.publicKey = publicKey;
    }

    public Card(CheckingAccount acc, Profile profile) {
        this.associatedAccount = acc.getId();
        this.associatedProfile = profile.getUserId();
        // gen CC num

        Random random = new Random(System.currentTimeMillis());
        String bin = "232323";
        int randomNumberLength = 16 - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);
        this.cardNumber = builder.toString();

    }

    private int getCheckDigit(String number) {

        // Luhn algo
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }


    @PersistenceConstructor
    private Card(String cardNumber, String associatedAccount, String associatedProfile, String publicKey) {
        this.cardNumber = cardNumber;
        this.associatedAccount = associatedAccount;
        this.associatedProfile = associatedProfile;
        this.publicKey = publicKey;
    }
}
