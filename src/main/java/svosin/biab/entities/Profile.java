package svosin.biab.entities;

import com.google.common.hash.Hashing;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
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
    @NotNull
    @Email
    String email;
    @NotNull
    String fullname;

    String passhash;


    Integer creditScore = 0;

    public Profile(String username, String plainpass) {
        this.username = username;
        this.passhash = Hashing.sha256().hashString(plainpass, StandardCharsets.UTF_8).toString();
    }

    public Profile(UserDTO userDTO) {
        this.username = userDTO.username;
        this.fullname = userDTO.fullName;
        this.passhash = Hashing.sha256().hashString(userDTO.plainpass, StandardCharsets.UTF_8).toString();
    }
}
