package pl.rowerek.maintenece;

import org.springframework.data.jpa.repository.JpaRepository;

interface MaintenanceRepository extends JpaRepository<Maintenance, MaintenanceId> {
}
