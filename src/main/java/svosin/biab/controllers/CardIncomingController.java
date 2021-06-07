package svosin.biab.controllers;

import org.javatuples.Pair;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import svosin.biab.entities.Card;
import svosin.biab.entities.CardPaymentRequest;
import svosin.biab.entities.SignedCardPaymentRequest;
import svosin.biab.services.CardsService;
import svosin.biab.services.KeystoreService;
import svosin.biab.services.PaymentProcessingService;

import javax.validation.Valid;

@RequestMapping(path = "/cardapi", produces = "application/json;charset=UTF-8")
@RestController
public class CardIncomingController {
    @Autowired
    PaymentProcessingService paymentProcessingService;
    @Autowired
    CardsService cardsService;
    @Autowired
    KeystoreService keystoreService;

    @PostMapping(value = "/payment", consumes = "application/json")
    public Pair<String, Boolean> processCardPayment(@RequestBody @Valid SignedCardPaymentRequest signedRequest) {
        CardPaymentRequest request = signedRequest.getRequest();
        String cardNumber = request.getCardNumber();
        String merchant = request.getMerchantName();
        Money amount = Money.parse(request.getAmount());
        String sig = signedRequest.getSignature();
        if(keystoreService.checkSignature(
                cardNumber,
                request.toString(),
                sig
        )) {
            Card card = cardsService.getCardByNumber(cardNumber);
            paymentProcessingService.payCard(merchant, card.getAssociatedAccount(), amount,
                    "*"+cardNumber.substring(11));
            return new Pair<>("ID GOES HERE", true);
        }
        return new Pair<>("ID GOES HERE", false);
    }
}