package pl.rowerek.restriction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.restriction.dto.CreateRestrictionDto;

import java.time.Instant;

@Service
@AllArgsConstructor
public class RestrictionService {

    private final RestrictionRepository restrictionRepository;
    private final BikeAvailabilityService bikeAvailabilityService;

    public RestrictionId addRestriction(CreateRestrictionDto createDto) {
        bikeAvailabilityService.takeBike
                (createDto.bikeId(), Instant.now(), createDto.to(), "Restriction");
        var bike = new Restriction(createDto.bikeId(), createDto.reason(), createDto.to());
        return restrictionRepository.save(bike).getId();
    }
}
