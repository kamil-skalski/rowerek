package pl.rowerek.availability;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.rowerek.bike.dto.BikeCreatedEvent;

@Component
@AllArgsConstructor
class BikeEventHandler {

    private BikeAvailabilityService bikeAvailabilityService;

    @TransactionalEventListener
    public void onBikeCreated(BikeCreatedEvent event) {
        bikeAvailabilityService.createBikeAvailability(event.id());
    }
}
