package svosin.biab.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document
public class Profile {
    @Id
    String userId;
    @NotNull
    String name;



    Integer creditScore;
}
