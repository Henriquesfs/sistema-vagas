package com.sistema.vagas.sistema_vagas.service;


import com.sistema.vagas.sistema_vagas.model.reservation.Reservation;
import com.sistema.vagas.sistema_vagas.model.reservation.ReservationRequestDTO;
import com.sistema.vagas.sistema_vagas.model.reservation.ReservationResponseDTO;
import com.sistema.vagas.sistema_vagas.model.spot.Spot;
import com.sistema.vagas.sistema_vagas.model.spot.SpotResponseDTO;
import com.sistema.vagas.sistema_vagas.model.user.User;
import com.sistema.vagas.sistema_vagas.model.user.UserResponseDTO;
import com.sistema.vagas.sistema_vagas.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SpotService spotService;

    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO) {

        validateReservationTimes(reservationRequestDTO.startDate(), reservationRequestDTO.endDate());


        User user = userService.findUserById(reservationRequestDTO.userId());
        Spot spot = spotService.findSpotById(reservationRequestDTO.spotId());


        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                reservationRequestDTO.spotId(),
                reservationRequestDTO.startDate(),
                reservationRequestDTO.endDate()
        );

        if (!overlappingReservations.isEmpty()) {
            throw new RuntimeException("Vaga já está reservada para este período");
        }


        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpotNumber(spot);
        reservation.setStartDate(reservationRequestDTO.startDate());
        reservation.setEndDate(reservationRequestDTO.endDate());

        Reservation savedReservation = reservationRepository.save(reservation);


        if (reservationRequestDTO.startDate().isBefore(LocalDateTime.now()) ||
                reservationRequestDTO.startDate().isEqual(LocalDateTime.now())) {
            spotService.updateSpotOccupancy(spot.getId(), true);
        }

        return convertToResponseDTO(savedReservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO getReservationById(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        return convertToResponseDTO(reservation);
    }

    public List<ReservationResponseDTO> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserIdOrderByStartDateDesc(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));


        spotService.updateSpotOccupancy(reservation.getSpotNumber().getId(), false);


        reservationRepository.delete(reservation);
    }

    @Transactional
    public void cancelReservation(Integer reservationId, Integer userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));


        if (!reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não pode cancelar esta reserva");
        }

        cancelReservation(reservationId);
    }

    public void processExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            spotService.updateSpotOccupancy(reservation.getSpotNumber().getId(), false);
        }
    }

    private void validateReservationTimes(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data de início não pode ser no passado");
        }

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new RuntimeException("Data de fim deve ser posterior à data de início");
        }
    }

    private ReservationResponseDTO convertToResponseDTO(Reservation reservation) {
        UserResponseDTO userDTO = new UserResponseDTO(
                reservation.getUser().getId(),
                reservation.getUser().getUsername()
        );

        SpotResponseDTO spotDTO = new SpotResponseDTO(
                reservation.getSpotNumber().getId(),
                reservation.getSpotNumber().getSpotNumber(),
                reservation.getSpotNumber().getSpotType(),
                reservation.getSpotNumber().isOccupied()
        );

        return new ReservationResponseDTO(
                reservation.getId(),
                userDTO,
                spotDTO,
                reservation.getStartDate(),
                reservation.getEndDate()
        );
    }
}
