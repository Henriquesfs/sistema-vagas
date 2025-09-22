package com.sistema.vagas.sistema_vagas.model.reservation;

import com.sistema.vagas.sistema_vagas.model.spot.SpotResponseDTO;
import com.sistema.vagas.sistema_vagas.model.user.UserResponseDTO;

import java.time.LocalDateTime;

public record ReservationResponseDTO(
        Integer id,
        UserResponseDTO user,
        SpotResponseDTO spot,
        LocalDateTime startDate,
        LocalDateTime endDate

) {
}
