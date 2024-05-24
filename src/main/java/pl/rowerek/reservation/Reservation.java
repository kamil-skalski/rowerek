package pl.rowerek.reservation;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.CustomerId;

import java.time.Instant;

@Entity
@Getter
class Reservation {

    @EmbeddedId
    private ReservationId reservationId;

    private Instant expiresAt;

    private Status status;

    public Reservation(BikeId bikeId, CustomerId customerId, Instant expiresAt) {
        this.reservationId = new ReservationId(bikeId.id(), customerId.id());
        this.expiresAt = expiresAt;
        this.status = Status.ACTIVE;
    }

    public Reservation() {
    }
}

