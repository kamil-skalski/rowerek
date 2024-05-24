package pl.rowerek.availability;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rowerek.bike.dto.BikeCreatedEvent;

@Component
@AllArgsConstructor
class BikeEventHandler {

    private BikeAvailabilityService bikeAvailabilityService;

    @EventListener
    public void onBikeCreated(BikeCreatedEvent event) {
        bikeAvailabilityService.createBikeAvailability(event.id());
    }
}
