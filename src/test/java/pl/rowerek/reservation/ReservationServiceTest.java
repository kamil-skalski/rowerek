package pl.rowerek.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.bike.BikeService;
import pl.rowerek.bike.dto.CreateBikeDto;
import pl.rowerek.common.*;
import pl.rowerek.customer.CustomerService;
import pl.rowerek.customer.dto.CreateCustomerDto;
import pl.rowerek.maintenece.MaintenanceService;
import pl.rowerek.maintenece.dto.CreateMaintenanceDto;
import pl.rowerek.restriction.RestrictionService;
import pl.rowerek.restriction.dto.CreateRestrictionDto;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationProperties reservationProperties;

    @Test
    void canReserveBike() {
        //given
        var bikeId = createBike();
        var customerId = createCustomer();
        var when = Instant.now();
        var reservationId = reservationService.reserve(bikeId, customerId, when);

        //when
        var reservation = reservationRepository.findById(reservationId).orElseThrow();

        //then
        assertEquals(customerId.id(), reservation.getReservationId().getCustomerId());
        assertEquals(bikeId.id(), reservation.getReservationId().getBikeId());
        assertThat(when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes())))
                .isCloseTo(reservation.getExpiresAt(), within(1, ChronoUnit.MILLIS));
        assertEquals(Status.ACTIVE, reservation.getStatus());
    }

    @Test
    void cannotReserveIfBikeIdNotExists() {
        //given
        var bikeId = BikeId.newOne();
        var customerId = createCustomer();
        var when = Instant.now();

        //expected
        assertThrows(BikeNotFoundException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    @Test
    void cannotReserveIfCustomerIdNotExists() {
        //given
        var bikeId = createBike();
        var customerId = CustomerId.newOne();
        var when = Instant.now();

        //expected
        assertThrows(CustomerNotFoundException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    @Test
    void cannotReserveIfBikeIsAlreadyReserved() {
        //given
        var bikeId = createBike();
        var customerId = createCustomer();
        var when = Instant.now();
        var otherCustomerId = createCustomer();
        var oneMinuteBeforeExpiresReservation = when.plus(Duration.ofMinutes(reservationProperties.getDurationMinutes() - 1));

        reservationService.reserve(bikeId, customerId, when);

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                reservationService.reserve(bikeId, otherCustomerId, oneMinuteBeforeExpiresReservation));
    }

    @Test
    void cannotReserveIfBikeIsUnderMaintenance() {
        //given
        var bikeId = createBike();
        var customerId = createCustomer();
        var when = Instant.now();
        maintenanceService.addMaintenance(new CreateMaintenanceDto(bikeId, "Naprawa"));

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    @Test
    void cannotReserveIfBikeIsUnderRestrictions() {
        //given
        var bikeId = createBike();
        var customerId = createCustomer();
        var when = Instant.now();
        restrictionService.addRestriction(new CreateRestrictionDto(bikeId, "Dzień konia", when.plus(Duration.ofHours(8))));

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    private CustomerId createCustomer() {
        CreateCustomerDto customerDto = new CreateCustomerDto("Waldemar", "Kiepski");
        return customerService.addCustomer(customerDto);
    }

    private BikeId createBike() {
        var bikeDto = new CreateBikeDto("Lowelek", "Czerwoniutki");
        return bikeService.addBike(bikeDto);
    }
}
