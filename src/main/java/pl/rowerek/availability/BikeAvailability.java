package pl.rowerek.availability;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import pl.rowerek.common.BikeId;

import java.time.Instant;

@Entity
class BikeAvailability {

    @EmbeddedId
    @Getter
    private BikeId bikeId;
    private Instant unavailableUntil;
    private String unavailabilityReason;
    private Instant lastUpdate;

    @Version
    private int version;

    public BikeAvailability(BikeId bikeId) {
        this.bikeId = bikeId;
        this.lastUpdate = Instant.now();
    }

    protected BikeAvailability() {
    }

    boolean take(Instant when, Instant until, String reason) {
        return false;
    }

    boolean isAvailable(Instant when) {
        return false;
    }

    void release() {

    }

    String getUnavailabilityReason(Instant when) {
        return null;
    }
}
