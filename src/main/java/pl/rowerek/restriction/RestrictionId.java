package pl.rowerek.restriction;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Embeddable
@EqualsAndHashCode
class RestrictionId {

    public static RestrictionId newOne() {
        return new RestrictionId(UUID.randomUUID());
    }

    private UUID restrictionId;

    public RestrictionId(UUID uuid) {
        this.restrictionId = uuid;
    }

    protected RestrictionId() {
    }

    public UUID id() {
        return restrictionId;
    }
}
