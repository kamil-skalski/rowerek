package pl.rowerek.availability;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.rowerek.common.BikeId;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAvailabilityBikesAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BikeAvailabilityService bikeAvailabilityService;

    @Test
    @DisplayName("""
            given exists available bikes,
            when request is sent to get available bikes,
            then return list of available bikes and HTTP 200 status""")
    void whenRequestIsSent_thenAvailableBikesReturnedAndHttp200() {
        //given
        var availableBikeId1 = BikeId.newOne();
        var availableBikeId2 = BikeId.newOne();
        var availableBikeId3 = BikeId.newOne();
        var availableBikeId4 = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(availableBikeId1);
        bikeAvailabilityService.createBikeAvailability(availableBikeId2);
        bikeAvailabilityService.createBikeAvailability(availableBikeId3);
        bikeAvailabilityService.createBikeAvailability(availableBikeId4);

        var unavailableBikeId1 = BikeId.newOne();
        var unavailableBikeId2 = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(unavailableBikeId1);
        bikeAvailabilityService.takeBike(unavailableBikeId1, Instant.now(), Instant.MAX, "Naprawa");
        bikeAvailabilityService.createBikeAvailability(unavailableBikeId2);
        bikeAvailabilityService.takeBike(unavailableBikeId2, Instant.now(), Instant.now().plus(Duration.ofMinutes(5)), "Rezerwacja");

        var takeAndReleaseBikeId = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(takeAndReleaseBikeId);
        bikeAvailabilityService.takeBike(takeAndReleaseBikeId, Instant.now(), Instant.MAX, "Restrykcja");
        bikeAvailabilityService.releaseBike(takeAndReleaseBikeId);

        //when
        ResponseEntity<BikeAvailabilityInfo[]> response = restTemplate.getForEntity(
                "/bikes/availability", BikeAvailabilityInfo[].class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .extracting(BikeAvailabilityInfo::bikeId)
                .hasSize(5)
                .containsExactlyInAnyOrder(availableBikeId1.id(), availableBikeId2.id(), availableBikeId3.id(), availableBikeId4.id(), takeAndReleaseBikeId.id());
    }
}
