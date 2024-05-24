package pl.rowerek.availability;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.common.BikeNotFoundException;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class BikeAvailabilityService {

    private final BikeAvailabilityRepository bikeAvailabilityRepository;

    @Transactional
    public void takeBike(BikeId bikeId, Instant from, Instant to, String reason) {
        var bike = bikeAvailabilityRepository.findById(bikeId)
                .orElseThrow(() -> new BikeNotFoundException(bikeId));

        if (!bike.take(from, to, reason)) {
            throw new BikeIsNotAvailableException(bikeId, bike.getUnavailabilityReason(from));
        }
    }

    @Transactional
    public void releaseBike(BikeId bikeId) {
        bikeAvailabilityRepository.findById(bikeId)
                .ifPresent(BikeAvailability::release);
    }

    @Transactional(readOnly = true)
    public List<BikeAvailabilityInfo> getAvailabilityBikes(Instant when) {
        return bikeAvailabilityRepository.findAllAvailableBikes(when)
                .stream().map(bike -> new BikeAvailabilityInfo(bike.getBikeId().id())).toList();
    }

    @Transactional(readOnly = true)
    public boolean isAvailabilityBike(BikeId bikeId, Instant when) {
        return bikeAvailabilityRepository.findById(bikeId).map(bike -> bike.isAvailable(when)).orElse(false);
    }

    public void createBikeAvailability(BikeId bikeId) {
        bikeAvailabilityRepository.save(new BikeAvailability(bikeId));
    }
}
