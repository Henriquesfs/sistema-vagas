package com.sistema.vagas.sistema_vagas.model.spot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpotRequestDTO (
        @NotBlank(message = "Número da vaga é obrigatório")
        String spotNumber,

        @NotNull(message = "Tipo da vaga é obrigatório")
        SpotType spotType,

        Boolean isOccupied
){
    public SpotRequestDTO {
        if (isOccupied == null) {
            isOccupied = false;
        }
    }
}
