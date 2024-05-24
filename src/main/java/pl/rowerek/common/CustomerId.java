package pl.rowerek.common;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Embeddable
@EqualsAndHashCode
public class CustomerId {

    public static CustomerId newOne() {
        return new CustomerId(UUID.randomUUID());
    }

    private UUID customerId;

    public CustomerId(UUID uuid) {
        this.customerId = uuid;
    }

    protected CustomerId() {
    }

    public UUID id() {
        return customerId;
    }
}
