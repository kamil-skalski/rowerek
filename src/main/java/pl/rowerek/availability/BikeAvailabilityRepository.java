package pl.rowerek.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rowerek.common.BikeId;

import java.time.Instant;
import java.util.List;

interface BikeAvailabilityRepository extends JpaRepository<BikeAvailability, BikeId> {

    @Query("SELECT b FROM BikeAvailability b WHERE b.unavailableUntil IS NULL OR b.unavailableUntil < :currentTime")
    List<BikeAvailability> findAllAvailableBikes(Instant currentTime);
}
