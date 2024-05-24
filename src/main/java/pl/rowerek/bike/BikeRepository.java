package pl.rowerek.bike;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rowerek.common.BikeId;

interface BikeRepository extends JpaRepository<Bike, BikeId> {
}
