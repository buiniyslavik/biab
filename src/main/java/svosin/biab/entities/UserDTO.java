package svosin.biab.entities;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
    @NotNull
    @NotEmpty
    String fullName;

    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String plainpass;

    @NotNull
    @Email
    String email;
}
