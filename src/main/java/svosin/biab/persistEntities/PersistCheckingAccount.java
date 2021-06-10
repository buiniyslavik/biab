package svosin.biab.persistEntities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.entities.Profile;

@Document
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistCheckingAccount {
    @Id
    String id;

    String currentBalance;

    Profile owner;

}
