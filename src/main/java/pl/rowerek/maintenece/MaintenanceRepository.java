package pl.rowerek.maintenece;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rowerek.common.BikeId;

interface MaintenanceRepository extends JpaRepository<Maintenance, MaintenanceId> {

    @Query("SELECT COUNT(m) > 0 FROM Maintenance m WHERE m.bikeId = :bikeId AND m.status = pl.rowerek.maintenece.Status.ACTIVE")
    boolean existsActiveMaintenanceByBikeId(BikeId bikeId);
}
