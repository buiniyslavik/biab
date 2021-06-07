package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.Card;
import svosin.biab.entities.CardKey;

import java.security.PublicKey;
import java.util.List;


public interface CardKeysRepository extends MongoRepository<CardKey, String> {
    public CardKey getByCard(String cardId);
}
