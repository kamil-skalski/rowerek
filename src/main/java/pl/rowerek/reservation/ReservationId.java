package pl.rowerek.reservation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
class ReservationId implements Serializable {
    private UUID bikeId;
    private UUID customerId;
}
