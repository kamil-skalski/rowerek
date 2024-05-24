package pl.rowerek.availability;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
class BikeAvailabilityController {
    private final BikeAvailabilityService bikeAvailabilityService;

    @GetMapping("bikes/availability")
    public List<BikeAvailabilityInfo> getAvailabilityBikes() {
        return bikeAvailabilityService.getAvailabilityBikes(Instant.now());
    }
}