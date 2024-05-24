package pl.rowerek.bike;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.rowerek.bike.dto.BikeCreatedEvent;
import pl.rowerek.bike.dto.BikeDto;
import pl.rowerek.bike.dto.CreateBikeDto;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeNotFoundException;

@Service
@AllArgsConstructor
public class BikeService {

    private final BikeRepository bikeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BikeDto getBike(BikeId id) {
        return bikeRepository.findById(id)
                .map(bike -> new BikeDto(id, bike.getModel(), bike.getColor()))
                .orElseThrow(() -> new BikeNotFoundException(id));
    }

    public BikeId addBike(CreateBikeDto createBikeDto) {
        var bike = new Bike(createBikeDto.model(), createBikeDto.color());
        var bikeId = bikeRepository.save(bike).getId();
        var bikeCreatedEvent = new BikeCreatedEvent(bikeId);
        applicationEventPublisher.publishEvent(bikeCreatedEvent);
        return bikeId;
    }

    public void checkBikeExists(BikeId id) {
        if (!bikeRepository.existsById(id)) {
            throw new BikeNotFoundException(id);
        }
    }
}