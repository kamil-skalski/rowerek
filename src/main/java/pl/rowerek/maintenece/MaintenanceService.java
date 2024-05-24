package pl.rowerek.maintenece;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.rowerek.bike.BikeService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.maintenece.dto.CreateMaintenanceDto;
import pl.rowerek.reservation.ReservationService;
import pl.rowerek.restriction.RestrictionService;

import java.time.Instant;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final RestrictionService restrictionService;
    private final ReservationService reservationService;
    private final BikeService bikeService;

    public MaintenanceService(MaintenanceRepository maintenanceRepository,
                              @Lazy RestrictionService restrictionService,
                              @Lazy ReservationService reservationService, BikeService bikeService) {
        this.maintenanceRepository = maintenanceRepository;
        this.restrictionService = restrictionService;
        this.reservationService = reservationService;
        this.bikeService = bikeService;
    }

    public boolean existsActiveMaintenanceByBikeId(BikeId bikeId) {
        return maintenanceRepository.existsActiveMaintenanceByBikeId(bikeId);
    }

    public MaintenanceId addMaintenance(CreateMaintenanceDto createDto) {
        bikeService.checkBikeExists(createDto.bikeId());

        if (restrictionService.isInRestriction(createDto.bikeId(), Instant.now())) {
            throw new BikeIsNotAvailableException("Bike is currently excluded due to external restrictions: BikeId: " + createDto.bikeId().id());
        }
        if (reservationService.isBikeReserved(createDto.bikeId(), Instant.now())) {
            throw new BikeIsNotAvailableException("Bike is currently reserved: BikeId: " + createDto.bikeId().id());
        }
        var bike = new Maintenance(createDto.bikeId(), createDto.reason());
        return maintenanceRepository.save(bike).getId();
    }

    @Transactional
    public void finishMaintenance(MaintenanceId maintenanceId) {
        maintenanceRepository.findById(maintenanceId)
                .ifPresent(Maintenance::finish);
    }
}
