package pl.rowerek.maintenece;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import pl.rowerek.common.BikeId;

@Entity
@Getter
class Maintenance {

    @EmbeddedId
    private MaintenanceId id;

    private BikeId bikeId;

    private String reason;

    private Status status;

    @Version
    private int version;

    public Maintenance(BikeId bikeId, String reason) {
        this.id = MaintenanceId.newOne();
        this.bikeId = bikeId;
        this.reason = reason;
        this.status = Status.ACTIVE;
    }

    public Maintenance() {
    }

    void finish() {
        this.status = Status.FINISHED;
    }
}
