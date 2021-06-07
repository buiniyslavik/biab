package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PublicKey;
import java.util.Random;

@Data
@NoArgsConstructor
@Document
public class Card {
    @Id
    String id;
    String cardNumber;

    @DBRef
    CheckingAccount associatedAccount;
    @DBRef
    Profile associatedProfile;

    PublicKey publicKey;
    //we store it in a different place for now

    public Card(Card card, PublicKey publicKey) {
        this.id = card.id;
        this.cardNumber = card.cardNumber;
        this.associatedAccount = card.associatedAccount;
        this.associatedProfile = card.associatedProfile;
        this.publicKey = publicKey;
    }

    public Card(CheckingAccount acc, Profile profile) {
        this.associatedAccount = acc;
        this.associatedProfile = profile;
        // gen CC num

        // The number of random digits that we need to generate is equal to the
        // total length of the card number minus the start digits given by the
        // user, minus the check digit at the end.
        Random random = new Random(System.currentTimeMillis());
        String bin = "232323";
        int randomNumberLength = 16 - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        // Do the Luhn algorithm to generate the check digit.
        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);
        this.cardNumber = builder.toString();

    }
    private int getCheckDigit(String number) {

        // Luhn algo
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        // The check digit is the number required to make the sum a multiple of
        // 10.
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

}
