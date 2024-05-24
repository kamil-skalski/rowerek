package pl.rowerek.reservation;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reservation")
@Getter
public class ReservationProperties {
    private int durationMinutes = 15;
}
