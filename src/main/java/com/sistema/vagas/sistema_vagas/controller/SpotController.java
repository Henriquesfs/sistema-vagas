package com.sistema.vagas.sistema_vagas.controller;

import com.sistema.vagas.sistema_vagas.model.spot.SpotRequestDTO;
import com.sistema.vagas.sistema_vagas.model.spot.SpotResponseDTO;
import com.sistema.vagas.sistema_vagas.model.spot.SpotType;
import com.sistema.vagas.sistema_vagas.service.SpotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin(origins = "*")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @PostMapping
    public ResponseEntity<SpotResponseDTO> createSpot(@Valid @RequestBody SpotRequestDTO spotRequestDTO) {
        try {
            SpotResponseDTO createdSpot = spotService.createSpot(spotRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SpotResponseDTO>> getAllSpots() {
        List<SpotResponseDTO> spots = spotService.getAllSpots();
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/available")
    public ResponseEntity<List<SpotResponseDTO>> getAvailableSpots() {
        List<SpotResponseDTO> availableSpots = spotService.getAvailableSpots();
        return ResponseEntity.ok(availableSpots);
    }

    @GetMapping("/type/{spotType}")
    public ResponseEntity<List<SpotResponseDTO>> getSpotsByType(@PathVariable SpotType spotType) {
        List<SpotResponseDTO> spots = spotService.getSpotsByType(spotType);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotResponseDTO> getSpotById(@PathVariable Integer id) {
        try {
            SpotResponseDTO spot = spotService.getSpotById(id);
            return ResponseEntity.ok(spot);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpotResponseDTO> updateSpot(@PathVariable Integer id,
                                                      @Valid @RequestBody SpotRequestDTO spotRequestDTO) {
        try {
            SpotResponseDTO updatedSpot = spotService.updateSpot(id, spotRequestDTO);
            return ResponseEntity.ok(updatedSpot);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Integer id) {
        try {
            spotService.deleteSpot(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
