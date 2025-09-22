package com.sistema.vagas.sistema_vagas.service;

import com.sistema.vagas.sistema_vagas.model.spot.Spot;
import com.sistema.vagas.sistema_vagas.model.spot.SpotRequestDTO;
import com.sistema.vagas.sistema_vagas.model.spot.SpotResponseDTO;
import com.sistema.vagas.sistema_vagas.model.spot.SpotType;
import com.sistema.vagas.sistema_vagas.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotService {
    @Autowired
    private SpotRepository spotRepository;

    public SpotResponseDTO createSpot(SpotRequestDTO spotRequestDTO) {
        if (spotRepository.existsBySpotNumber(spotRequestDTO.spotNumber())) {
            throw new RuntimeException("Número da vaga já existe");
        }

        Spot spot = new Spot();
        spot.setSpotNumber(spotRequestDTO.spotNumber());
        spot.setSpotType(spotRequestDTO.spotType());
        spot.setOccupied(spotRequestDTO.isOccupied());

        Spot savedSpot = spotRepository.save(spot);
        return convertToResponseDTO(savedSpot);
    }

    public List<SpotResponseDTO> getAllSpots() {
        return spotRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SpotResponseDTO> getAvailableSpots() {
        return spotRepository.findAvailableSpots().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SpotResponseDTO> getSpotsByType(SpotType spotType) {
        return spotRepository.findBySpotType(spotType).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public SpotResponseDTO getSpotById(Integer id) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        return convertToResponseDTO(spot);
    }

    public SpotResponseDTO updateSpot(Integer id, SpotRequestDTO spotRequestDTO) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));


        if (!spot.getSpotNumber().equals(spotRequestDTO.spotNumber()) &&
                spotRepository.existsBySpotNumber(spotRequestDTO.spotNumber())) {
            throw new RuntimeException("Número da vaga já existe");
        }

        spot.setSpotNumber(spotRequestDTO.spotNumber());
        spot.setSpotType(spotRequestDTO.spotType());
        spot.setOccupied(spotRequestDTO.isOccupied());

        Spot updatedSpot = spotRepository.save(spot);
        return convertToResponseDTO(updatedSpot);
    }

    public void deleteSpot(Integer id) {
        if (!spotRepository.existsById(id)) {
            throw new RuntimeException("Vaga não encontrada");
        }
        spotRepository.deleteById(id);
    }

    public void updateSpotOccupancy(Integer spotId, boolean isOccupied) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        spot.setOccupied(isOccupied);
        spotRepository.save(spot);
    }

    public Spot findSpotById(Integer id) {
        return spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
    }

    private SpotResponseDTO convertToResponseDTO(Spot spot) {
        return new SpotResponseDTO(spot.getId(), spot.getSpotNumber(),
                spot.getSpotType(), spot.isOccupied());
    }
}
