package svosin.biab.services;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import svosin.biab.entities.Card;
import svosin.biab.entities.CardPaymentNonce;
import svosin.biab.repos.CardNonceRepository;
import svosin.biab.repos.CardRepository;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class KeystoreService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardNonceRepository cardNonceRepository;

    @SneakyThrows
    public Pair<Card, PrivateKey> createCardKey(Card card) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        Card card1 = new Card(card, Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        //cardKeysRepository.save(new CardKey(keyPair.getPublic(), forCardId));
        return new Pair<>(card1, keyPair.getPrivate());
    }

    @SneakyThrows
    public boolean checkSignature(String forCardId, String data, String sig) {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(
                new X509EncodedKeySpec(
                        Base64.getDecoder().decode(
                                cardRepository.getByCardNumber(forCardId).getPublicKey()
                        )
                )
        );


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(key);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sig));

    }

    public CardPaymentNonce createNonceForCard(String cardNumber) {
        var nonce = new CardPaymentNonce(cardNumber);
        return cardNonceRepository.save(nonce);
    }

    public List<CardPaymentNonce> getNonceForCard(String cardNumber) {
        return cardNonceRepository.getCardPaymentNonceByCardNumber(cardNumber);
    }

    public void destroyNonce(CardPaymentNonce nonce) {
        cardNonceRepository.delete(nonce);
    }
}
