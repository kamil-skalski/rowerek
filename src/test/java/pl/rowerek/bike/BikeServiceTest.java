package pl.rowerek.bike;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.bike.dto.CreateBikeDto;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BikeServiceTest {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BikeAvailabilityService bikeAvailabilityService;

    @Test
    void shouldCreateBikeAvailabilityWhenBikeIsAdded() {
        //given
        var createDto = new CreateBikeDto("szybki bo", "czerwony");

        //when
        var bikeId = bikeService.addBike(createDto);

        //then
        assertThat(bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now())).isTrue();
    }
}