package pl.rowerek.availability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.common.BikeNotFoundException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BikeAvailabilityServiceTest {

    @Autowired
    private BikeAvailabilityService bikeAvailabilityService;

    @Autowired
    private BikeAvailabilityRepository bikeAvailabilityRepository;

    @Test
    void canCreateAvailabilityForBike() {
        //given
        var bikeId = BikeId.newOne();

        //when
        bikeAvailabilityService.createBikeAvailability(bikeId);

        //then
        var bikeAvailability = bikeAvailabilityRepository.findById(bikeId).orElseThrow();
        assertNotNull(bikeAvailability);
        assertTrue(bikeAvailability.isAvailable(Instant.now()));
    }

    @Test
    void cannotTakeNotExistingBike() {
        //given
        var notExistingId = BikeId.newOne();
        var from = Instant.now();
        var to = from.plusSeconds(3600);
        var reason = "Naprawie to!";

        //expected
        assertThrows(BikeNotFoundException.class, () ->
                bikeAvailabilityService.takeBike(notExistingId, from, to, reason));
    }

    @Test
    void canTakeAvailableBike() {
        //given
        var bikeId = createBike();
        var from = Instant.now();
        var to = from.plusSeconds(3600);
        var reason = "Naprawie to!";

        //when
        bikeAvailabilityService.takeBike(bikeId, from, to, reason);

        //then
        var bike = bikeAvailabilityRepository.findById(bikeId).orElseThrow();
        assertNotNull(bike);
        assertFalse(bike.isAvailable(from.plusSeconds(1)));
        assertTrue(bike.isAvailable(to.plusSeconds(1)));
        assertEquals(reason, bike.getUnavailabilityReason(from.plusSeconds(1)));
    }

    @Test
    void cannotTakeUnavailableBike() {
        var bikeId = createBike();
        var from = Instant.now();
        var to = from.plusSeconds(3600);
        var reason = "Naprawie to!";
        bikeAvailabilityService.takeBike(bikeId, from, to, reason);

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                bikeAvailabilityService.takeBike(bikeId, from, to, reason));
    }

    @Test
    void canReleaseBikeBike() {
        //given
        var bikeId = createBike();
        var from = Instant.now();
        var to = from.plusSeconds(3600);
        var reason = "Naprawie to!";
        bikeAvailabilityService.takeBike(bikeId, from, to, reason);

        //when
        bikeAvailabilityService.releaseBike(bikeId);

        var bike = bikeAvailabilityRepository.findById(bikeId).orElseThrow();
        assertNotNull(bike);
        assertTrue(bike.isAvailable(from.plusSeconds(1)));
        assertTrue(bike.isAvailable(to.plusSeconds(1)));
        assertNull(bike.getUnavailabilityReason(from.plusSeconds(1)));
    }

    @Test
    void canTakeBikeAfterReleaseBike() {
        //given
        var bikeId = createBike();
        var from = Instant.now();
        var to = from.plusSeconds(3600);
        var reason = "Naprawie to!";
        bikeAvailabilityService.takeBike(bikeId, from, to, reason);
        bikeAvailabilityService.releaseBike(bikeId);

        //when
        bikeAvailabilityService.takeBike(bikeId, from, to, reason);

        //then
        var bike = bikeAvailabilityRepository.findById(bikeId).orElseThrow();
        assertNotNull(bike);
        assertFalse(bike.isAvailable(from.plusSeconds(1)));
        assertTrue(bike.isAvailable(to.plusSeconds(1)));
        assertEquals(reason, bike.getUnavailabilityReason(from.plusSeconds(1)));
    }

    @Test
    void canCheckBikeAvailability() {
        //given
        var bikeId = createBike();
        bikeAvailabilityService.takeBike(bikeId, Instant.now(), Instant.now().plusSeconds(3600), "Chce to biere");

        //when
        boolean isAvailableNow = bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now());
        boolean isAvailableLater = bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now().plusSeconds(3600));

        //then
        assertFalse(isAvailableNow);
        assertTrue(isAvailableLater);
    }

    private BikeId createBike() {
        var bikeId = BikeId.newOne();
        bikeAvailabilityRepository.save(new BikeAvailability(bikeId));
        return bikeId;
    }
}