package svosin.biab.entities;

import com.google.common.hash.Hashing;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("UnstableApiUsage")

@Data
@Document
public class Profile {
    @Id
    String userId;
    @NotNull
    String username;
    String passhash;


    Integer creditScore = 0;

    public Profile(String username, String plainpass) {
        this.username = username;
        this.passhash = Hashing.sha256().hashString(plainpass, StandardCharsets.UTF_8).toString();
    }
}
