package com.sistema.vagas.sistema_vagas.model.spot;

public record SpotResponseDTO(
        Integer id,
        String spotNumber,
        SpotType spotType,
        boolean isOccupied
) {
}
