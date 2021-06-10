package svosin.biab.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        this.passhash = bcpe.encode(plainpass);
    }

    public Profile(UserDTO userDTO) {
        this.username = userDTO.username;
        this.fullname = userDTO.fullName;
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        this.passhash = bcpe.encode(userDTO.getPlainpass());
    }
}
