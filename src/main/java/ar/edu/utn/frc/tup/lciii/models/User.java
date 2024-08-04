package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long id;

    private String userName;

    private String email;
}
