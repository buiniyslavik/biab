package svosin.biab.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutOfFundsException extends Exception{
    public OutOfFundsException(String msg) {
        super(msg);
    }
}
