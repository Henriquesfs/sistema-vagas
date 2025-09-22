package com.sistema.vagas.sistema_vagas.repository;

import com.sistema.vagas.sistema_vagas.model.spot.Spot;
import com.sistema.vagas.sistema_vagas.model.spot.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Integer> {
    Optional<Spot> findBySpotNumber(String spotNumber);
    boolean existsBySpotNumber(String spotNumber);
    List<Spot> findBySpotType(SpotType spotType);
    List<Spot> findByIsOccupied(boolean isOccupied);

    @Query("SELECT s FROM Spot s WHERE s.isOccupied = false")
    List<Spot> findAvailableSpots();
}
