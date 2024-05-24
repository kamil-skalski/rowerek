package pl.rowerek.common;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Embeddable
@EqualsAndHashCode
public class BikeId {

    public static BikeId newOne() {
        return new BikeId(UUID.randomUUID());
    }

    private UUID bikeId;

    public BikeId(UUID uuid) {
        this.bikeId = uuid;
    }

    protected BikeId() {
    }

    public UUID id() {
        return bikeId;
    }
}
