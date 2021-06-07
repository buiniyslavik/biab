package svosin.biab.services;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.Card;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.repos.CardKeysRepository;
import svosin.biab.repos.CardRepository;

import java.security.PrivateKey;
import java.util.List;

@Service
public class CardsService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    KeystoreService keystoreService;

    public Pair<Card, PrivateKey> createCard(CheckingAccount forAcc, Profile profile) {
        Card cardTemplate = new Card(forAcc, profile);
        var keyedCardPair = keystoreService.createCardKey(cardTemplate);
        cardRepository.save(keyedCardPair.getValue0()); //we don't store priv key
        return keyedCardPair;
    }

    public Card getCardByNumber(String number) {
        return cardRepository.getByCardNumber(number);
    }
    public List<Card> getProfileCardList(Profile profile) {
        return cardRepository.getAllByAssociatedProfile(profile);
    }
}
