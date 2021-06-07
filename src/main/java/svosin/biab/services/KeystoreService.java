package svosin.biab.services;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import svosin.biab.entities.Card;
import svosin.biab.entities.CardKey;
import svosin.biab.repos.CardKeysRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.*;

@Service
public class KeystoreService {
    @Autowired
    CardKeysRepository cardKeysRepository;

    //    private KeyStore keyStore;
/*
    @SneakyThrows
    public KeystoreService() {
        keyStore = KeyStore.getInstance("pcks12");
        char[] pwdArray = "CHANGEME".toCharArray();
        try {
            keyStore.load(new FileInputStream("cardsKeyStore.pkcs12"), pwdArray);
        } catch(FileNotFoundException e) {
            keyStore.load(null, pwdArray);
        }
        try (FileOutputStream fos = new FileOutputStream("cardsKeyStore.pkcs12")) {
            keyStore.store(fos, pwdArray);
        }
    }
*/
    @SneakyThrows
    public PrivateKey generateRsaKeyPair(String forCardId) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        cardKeysRepository.save(new CardKey(keyPair.getPublic(), forCardId));
        return keyPair.getPrivate();
    }

    @SneakyThrows
    public Pair<Card, PrivateKey> createCardKey(Card card) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        Card card1 = new Card(card, keyPair.getPublic());
        //cardKeysRepository.save(new CardKey(keyPair.getPublic(), forCardId));
        return new Pair<Card, PrivateKey>(card1, keyPair.getPrivate());
    }

    @SneakyThrows
    public boolean checkSignature(String forCardId, String data, String sig) {
        PublicKey publicKey = cardKeysRepository.getByCard(forCardId).getPublicKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(sig.getBytes());

    }
}
