package svosin.biab.entities;


import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.persistEntities.PersistCheckingAccount;

@Data
public class CheckingAccount {
    @Id
    String id;

    Money currentBalance;

    @DBRef
    Profile owner;


    public CheckingAccount(Profile owner) {
        this.owner = owner;
        this.currentBalance = Money.zero(Ruble.rub);

    }
    public CheckingAccount(PersistCheckingAccount p) {
        this.id = p.getId();
        this.owner = p.getOwner();
        this.currentBalance = Money.parse(p.getCurrentBalance());
    }

    public PersistCheckingAccount toPersist() {
        return new PersistCheckingAccount(id, currentBalance.toString(), owner);
    }
}
