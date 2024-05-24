package pl.rowerek.maintenece;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.maintenece.dto.CreateMaintenanceDto;

import java.time.Instant;

@Service
@AllArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final BikeAvailabilityService bikeAvailabilityService;

    public MaintenanceId addMaintenance(CreateMaintenanceDto createDto) {
        bikeAvailabilityService.takeBike
                (createDto.bikeId(), Instant.now(), Instant.MAX, "Maintenance");

        var maintenance = new Maintenance(createDto.bikeId(), createDto.reason());
        return maintenanceRepository.save(maintenance).getId();
    }

    @Transactional
    public void finishMaintenance(MaintenanceId maintenanceId) {
        maintenanceRepository.findById(maintenanceId)
                .ifPresent(maintenance -> {
                    maintenance.finish();
                    bikeAvailabilityService.releaseBike(maintenance.getBikeId());
                });
    }
}
