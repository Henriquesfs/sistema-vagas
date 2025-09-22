package com.sistema.vagas.sistema_vagas.model.spot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "spots")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "spot_number", nullable = false)
    private String spotNumber;

    @Column(name = "spot_type", nullable = false)
    private SpotType spotType;

    @Column(name = "occupied")
    private boolean isOccupied;
}
