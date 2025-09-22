package com.sistema.vagas.sistema_vagas.repository;

import com.sistema.vagas.sistema_vagas.model.reservation.Reservation;
import com.sistema.vagas.sistema_vagas.model.spot.Spot;
import com.sistema.vagas.sistema_vagas.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findBySpotNumber(Spot spot);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId ORDER BY r.startDate DESC")
    List<Reservation> findByUserIdOrderByStartDateDesc(@Param("userId") Integer userId);

    @Query("SELECT r FROM Reservation r WHERE r.spotNumber.id = :spotId " +
            "AND r.startDate < :endDate AND r.endDate > :startDate")
    List<Reservation> findOverlappingReservations(
            @Param("spotId") Integer spotId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT r FROM Reservation r WHERE r.endDate < :currentTime")
    List<Reservation> findExpiredReservations(@Param("currentTime") LocalDateTime currentTime);
}
