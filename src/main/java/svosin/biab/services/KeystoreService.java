package svosin.biab.services;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import svosin.biab.entities.Card;
import svosin.biab.repos.CardRepository;

import java.security.*;
import java.util.Arrays;

@Service
public class KeystoreService {
    @Autowired
    CardRepository cardRepository;

    @SneakyThrows
    public Pair<Card, PrivateKey> createCardKey(Card card) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        Card card1 = new Card(card, Arrays.toString(keyPair.getPublic().getEncoded()));
        //cardKeysRepository.save(new CardKey(keyPair.getPublic(), forCardId));
        return new Pair<Card, PrivateKey>(card1, keyPair.getPrivate());
    }

    @SneakyThrows
    public boolean checkSignature(String forCardId, String data, String sig) {
        PublicKey key = new PublicKey() {
            @Override
            public String getAlgorithm() {
                return "RSA";
            }

            @Override
            public String getFormat() {
                return "X.509";
            }

            @Override
            public byte[] getEncoded() {
                return cardRepository.getById(forCardId).getPublicKey().getBytes();
            }
        };


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(key);
        signature.update(data.getBytes());
        return signature.verify(sig.getBytes());

    }
}
