package pl.rowerek.maintenece;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Embeddable
@EqualsAndHashCode
class MaintenanceId {

    public static MaintenanceId newOne() {
        return new MaintenanceId(UUID.randomUUID());
    }

    private UUID maintenanceId;

    public MaintenanceId(UUID uuid) {
        this.maintenanceId = uuid;
    }

    protected MaintenanceId() {
    }

    public UUID id() {
        return maintenanceId;
    }
}
