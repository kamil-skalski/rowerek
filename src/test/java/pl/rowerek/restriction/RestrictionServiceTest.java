package pl.rowerek.restriction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.common.BikeNotFoundException;
import pl.rowerek.restriction.dto.CreateRestrictionDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestrictionServiceTest {

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private BikeAvailabilityService bikeAvailabilityService;

    @Test
    void cannotAddRestrictionIfBikeIdNotExists() {
        //given
        var notExistingBikeId = BikeId.newOne();
        var reason = "Bieg konia";
        var createDto = new CreateRestrictionDto(notExistingBikeId, reason, Instant.now().plusSeconds(100));

        //expected
        assertThrows(BikeNotFoundException.class, () ->
                restrictionService.addRestriction(createDto));
    }

    @Test
    void canAddRestrictionAndMakeBikeUnavailable() {
        //given
        var bikeId = createAvailableBike();
        var reason = "Bieg konia";
        var endOfRestriction = Instant.now().plusSeconds(500);
        var createDto = new CreateRestrictionDto(bikeId, reason, endOfRestriction);

        //when
        var restrictionId = restrictionService.addRestriction(createDto);

        //then
        var restriction = restrictionRepository.findById(restrictionId).orElseThrow();
        assertEquals(bikeId, restriction.getBikeId());
        assertEquals(reason, restriction.getReason());
        assertThat(endOfRestriction)
                .isCloseTo(restriction.getEndAt(), within(1, ChronoUnit.MILLIS));
        assertFalse(bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now()));
    }

    @Test
    void cannotAddRestrictionForUnavailableBike() {
        //given
        var bikeId = createAvailableBike();
        var reason = "Bieg konia";
        var createDto = new CreateRestrictionDto(bikeId, reason, Instant.now().plusSeconds(100));
        bikeAvailabilityService.takeBike(bikeId, Instant.now(), Instant.MAX, "Bo tak");

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                restrictionService.addRestriction(createDto));
    }

    private BikeId createAvailableBike() {
        var bikeId = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(bikeId);
        return bikeId;
    }
}