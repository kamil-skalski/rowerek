package pl.rowerek.restriction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rowerek.common.BikeId;

import java.time.Instant;

interface RestrictionRepository extends JpaRepository<Restriction, RestrictionId> {

    @Query("SELECT COUNT(r) > 0 FROM Restriction r WHERE r.bikeId = :bikeId AND r.endAt > :currentTime")
    boolean existsActiveRestrictionByBikeId(@Param("bikeId") BikeId bikeId, @Param("currentTime") Instant currentTime);
}
