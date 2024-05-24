package pl.rowerek.reservation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.CustomerId;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProperties reservationProperties;

    ReservationId reserve(BikeId bikeId, CustomerId customerId, Instant when) {
        //TODO

        var newReservation = new Reservation(bikeId, customerId, when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes())));
        return reservationRepository.save(newReservation).getReservationId();
    }

    public boolean isBikeReserved(BikeId bikeId, Instant when) {
        var reservations = reservationRepository.findByReservationIdBikeIdAndStatus(bikeId.id(), Status.ACTIVE);
        return reservations.stream()
                .anyMatch(reservation -> reservation.getExpiresAt().isAfter(when));
    }
}