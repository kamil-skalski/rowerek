package pl.rowerek.restriction;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import pl.rowerek.common.BikeId;

import java.time.Instant;

@Entity
@Getter
class Restriction {

    @EmbeddedId
    private RestrictionId id;

    private BikeId bikeId;

    private String reason;

    private Instant endAt;

    @Version
    private int version;

    public Restriction(BikeId bikeId, String reason, Instant endAt) {
        this.id = RestrictionId.newOne();
        this.bikeId = bikeId;
        this.reason = reason;
        this.endAt = endAt;
    }

    public Restriction() {
    }

    boolean expired(Instant when) {
        return when.isAfter(endAt);
    }
}
