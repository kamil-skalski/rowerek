package pl.rowerek.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rowerek.common.BikeId;

interface BikeAvailabilityRepository extends JpaRepository<BikeAvailability, BikeId> {
}
