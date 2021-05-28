package svosin.biab.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import svosin.biab.creditProviders.NullCreditProvider;
import svosin.biab.interfaces.MoneyCreditProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PaymentProcessingService {
    public List<MoneyCreditProvider> getAllCreditProviders() {
        return Stream.of(
                new NullCreditProvider()
        ).collect(Collectors.toList());
    }


}
