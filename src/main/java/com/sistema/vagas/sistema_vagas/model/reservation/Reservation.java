package com.sistema.vagas.sistema_vagas.model.reservation;


import com.sistema.vagas.sistema_vagas.model.spot.Spot;
import com.sistema.vagas.sistema_vagas.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spotNumber;

    @Column(name = "start_time")
    private LocalDateTime startDate;

    @Column(name = "end_time")
    private LocalDateTime endDate;

}
