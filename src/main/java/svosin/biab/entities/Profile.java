package svosin.biab.entities;

import com.google.common.hash.Hashing;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("UnstableApiUsage")

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        this.passhash = bcpe.encode(userDTO.getPlainpass());
        System.out.println(this.username);
        System.out.println(this.fullname);
        System.out.println(userDTO.getPlainpass());
        System.out.println(this.passhash);
    }
}
