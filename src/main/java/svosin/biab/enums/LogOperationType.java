package svosin.biab.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LogOperationType {
    DEBIT("Платёж"),
    CREDIT("Пополнение");

    @Getter private String value;
}
