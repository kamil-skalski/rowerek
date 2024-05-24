package pl.rowerek.bike;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.Getter;
import pl.rowerek.common.BikeId;

@Entity
@Getter
class Bike {

    @EmbeddedId
    private BikeId id;

    private String model;

    private String color;

    @Version
    private int version;

    public Bike(String model, String color) {
        this.id = BikeId.newOne();
        this.model = model;
        this.color = color;
    }

    public Bike() {
    }
}
