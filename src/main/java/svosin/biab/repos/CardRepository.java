package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.Card;
import svosin.biab.entities.Profile;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
    public Card getByCardNumber(String number);
    public List<Card> getAllByAssociatedProfile(Profile profile);
}
