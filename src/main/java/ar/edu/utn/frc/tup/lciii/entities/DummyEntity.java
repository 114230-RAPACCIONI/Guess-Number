package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DummyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String dummy;
}
