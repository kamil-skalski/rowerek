package pl.rowerek.reservation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.bike.BikeService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.common.CustomerId;
import pl.rowerek.customer.CustomerService;
import pl.rowerek.maintenece.MaintenanceService;
import pl.rowerek.restriction.RestrictionService;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProperties reservationProperties;
    private final BikeService bikeService;
    private final CustomerService customerService;
    private final MaintenanceService maintenanceService;
    private final RestrictionService restrictionService;

    ReservationId reserve(BikeId bikeId, CustomerId customerId, Instant when) {
        bikeService.checkBikeExists(bikeId);
        customerService.checkCustomerExists(customerId);

        if (isBikeReserved(bikeId, when)) {
            throw new BikeIsNotAvailableException("Bike is already reserved: BikeId: " + bikeId.id());
        }

        if (maintenanceService.existsActiveMaintenanceByBikeId(bikeId)) {
            throw new BikeIsNotAvailableException("Bike is currently undergoing maintenance: BikeId: " + bikeId.id());
        }

        if (restrictionService.isInRestriction(bikeId, when)) {
            throw new BikeIsNotAvailableException("Bike is currently excluded due to external restrictions: BikeId: " + bikeId.id());
        }

        var newReservation = new Reservation(bikeId, customerId, when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes())));
        return reservationRepository.save(newReservation).getReservationId();
    }

    public boolean isBikeReserved(BikeId bikeId, Instant when) {
        var reservations = reservationRepository.findByReservationIdBikeIdAndStatus(bikeId.id(), Status.ACTIVE);
        return reservations.stream()
                .anyMatch(reservation -> reservation.getExpiresAt().isAfter(when));
    }
}