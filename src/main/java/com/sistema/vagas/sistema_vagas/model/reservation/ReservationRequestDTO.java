package com.sistema.vagas.sistema_vagas.model.reservation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequestDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        Integer userId,

        @NotNull(message = "ID da vaga é obrigatório")
        Integer spotId,

        @NotNull(message = "Data de início é obrigatória")
        LocalDateTime startDate,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDateTime endDate
) {
}
