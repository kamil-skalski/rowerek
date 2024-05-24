package pl.rowerek.reservation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.CustomerId;
import pl.rowerek.customer.CustomerService;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProperties reservationProperties;
    private final CustomerService customerService;
    private final BikeAvailabilityService bikeAvailabilityService;

    ReservationId reserve(BikeId bikeId, CustomerId customerId, Instant when) {
        customerService.checkCustomerExists(customerId);

        bikeAvailabilityService.takeBike
                (bikeId, when, when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes())), "Reservation");

        var newReservation = new Reservation(bikeId, customerId, when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes())));
        return reservationRepository.save(newReservation).getReservationId();
    }
}