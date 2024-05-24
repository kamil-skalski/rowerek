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
        if (until.isBefore(when)) {
            throw new IllegalArgumentException("The end time 'until' must be after the start time 'when'.");
        }
        if (isAvailable(when)) {
            unavailableUntil = until;
            unavailabilityReason = reason;
            lastUpdate = Instant.now();
            return true;
        }
        return false;
    }

    boolean isAvailable(Instant when) {
        return unavailableUntil == null || unavailableUntil.isBefore(when);
    }

    void release() {
        unavailableUntil = null;
        unavailabilityReason = null;
        lastUpdate = Instant.now();
    }

    String getUnavailabilityReason(Instant when) {
        if (!isAvailable(when)) {
            return unavailabilityReason;
        }
        return null;
    }
}
