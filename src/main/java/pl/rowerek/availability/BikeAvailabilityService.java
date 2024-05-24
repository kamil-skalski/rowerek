package pl.rowerek.availability;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.common.BikeId;

import java.time.Instant;
import java.util.List;


@Service
@AllArgsConstructor
public class BikeAvailabilityService {

    private final BikeAvailabilityRepository bikeAvailabilityRepository;


    public void takeBike(BikeId bikeId, Instant from, Instant to, String reason) {

    }

    public void releaseBike(BikeId bikeId) {

    }

    public List<BikeAvailabilityInfo> getAvailabilityBikes(Instant when) {
        return null;
    }

    public boolean isAvailabilityBike(BikeId bikeId, Instant when) {
        return false;
    }

    public void createBikeAvailability(BikeId bikeId) {
    }
}