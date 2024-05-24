package pl.rowerek.bike;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.bike.dto.BikeDto;
import pl.rowerek.bike.dto.CreateBikeDto;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeNotFoundException;

@Service
@AllArgsConstructor
public class BikeService {

    private final BikeRepository bikeRepository;

    public BikeDto getBike(BikeId id) {
        return bikeRepository.findById(id)
                .map(bike -> new BikeDto(id, bike.getModel(), bike.getColor()))
                .orElseThrow(() -> new BikeNotFoundException(id));
    }

    public BikeId addBike(CreateBikeDto createBikeDto) {
        var bike = new Bike(createBikeDto.model(), createBikeDto.color());
        return bikeRepository.save(bike).getId();
    }

    public void checkBikeExists(BikeId id) {
        if (!bikeRepository.existsById(id)) {
            throw new BikeNotFoundException(id);
        }
    }
}