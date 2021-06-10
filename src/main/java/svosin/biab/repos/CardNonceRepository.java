package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CardPaymentNonce;

import java.util.List;

public interface CardNonceRepository extends MongoRepository<CardPaymentNonce, String> {
    public List<CardPaymentNonce> getCardPaymentNonceByCardNumber(String cardNumber);

}
