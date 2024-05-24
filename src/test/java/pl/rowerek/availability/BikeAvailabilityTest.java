package pl.rowerek.availability;

import org.junit.jupiter.api.Test;
import pl.rowerek.common.BikeId;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class BikeAvailabilityTest {

    @Test
    void takeAvailableBikeTest() {
        var bike = new BikeAvailability(BikeId.newOne());
        var now = Instant.now();
        assertTrue(bike.take(now, now.plusSeconds(3600), "Biere bo moge"));
        assertEquals("Biere bo moge", bike.getUnavailabilityReason(now));
    }

    @Test
    void takeUnavailableBikeTest() {
        var bike = new BikeAvailability(BikeId.newOne());
        var now = Instant.now();
        bike.take(now, now.plusSeconds(1800), "Biere bo moge");
        assertFalse(bike.take(now, now.plusSeconds(3600), "Biere bo moge"));
    }

    @Test
    void isAvailableTest() {
        var bike = new BikeAvailability(BikeId.newOne());
        var now = Instant.now();
        assertTrue(bike.isAvailable(now));
        bike.take(now, now.plusSeconds(1800), "Biere bo moge");
        assertTrue(bike.isAvailable(now.plusSeconds(1801)));
        assertFalse(bike.isAvailable(now.plusSeconds(1799)));
    }

    @Test
    void releaseTest() {
        var bike = new BikeAvailability(BikeId.newOne());
        var now = Instant.now();
        bike.take(now, now.plusSeconds(3600), "Biere bo moge");
        assertNotNull(bike.getUnavailabilityReason(now));
        bike.release();
        assertNull(bike.getUnavailabilityReason(now));
    }

    @Test
    void getUnavailabilityReasonTest() {
        var bike = new BikeAvailability(BikeId.newOne());
        var now = Instant.now();
        assertNull(bike.getUnavailabilityReason(now));
        bike.take(now, now.plusSeconds(3600), "Biere bo moge");
        assertEquals("Biere bo moge", bike.getUnavailabilityReason(now));
    }
}