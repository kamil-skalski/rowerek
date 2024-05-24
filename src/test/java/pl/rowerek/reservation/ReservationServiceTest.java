package pl.rowerek.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.common.*;
import pl.rowerek.customer.CustomerService;
import pl.rowerek.customer.dto.CreateCustomerDto;

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
    private BikeAvailabilityService bikeAvailabilityService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationProperties reservationProperties;

    @Test
    void cannotReserveIfBikeIdNotExists() {
        //given
        var notExistingBikeId = BikeId.newOne();
        var customerId = createCustomer();
        var when = Instant.now();

        //expected
        assertThrows(BikeNotFoundException.class, () ->
                reservationService.reserve(notExistingBikeId, customerId, when));
    }

    @Test
    void cannotReserveIfCustomerIdNotExists() {
        //given
        var bikeId = createAvailableBike();
        var customerId = CustomerId.newOne();
        var when = Instant.now();

        //expected
        assertThrows(CustomerNotFoundException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    @Test
    void canReserveAvailableBike() {
        //given
        var bikeId = createAvailableBike();
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
    void cannotReserveUnavailableBike() {
        //given
        var bikeId = createAvailableBike();
        var customerId = createCustomer();
        var when = Instant.now();
        bikeAvailabilityService.takeBike(bikeId, when, when.plus(Duration.ofMinutes(1)), "Bo moge");

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                reservationService.reserve(bikeId, customerId, when));
    }

    private CustomerId createCustomer() {
        CreateCustomerDto customerDto = new CreateCustomerDto("Waldemar", "Kiepski");
        return customerService.addCustomer(customerDto);
    }

    private BikeId createAvailableBike() {
        var bikeId = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(bikeId);
        return bikeId;
    }
}
