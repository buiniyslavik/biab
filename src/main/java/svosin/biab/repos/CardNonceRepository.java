package svosin.biab.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import svosin.biab.entities.CardPaymentNonce;

import java.util.List;
import java.util.Map;

public interface CardNonceRepository extends MongoRepository<CardPaymentNonce, String> {
    public List<CardPaymentNonce> getCardPaymentNonceByCardNumber(String cardNumber);

}
