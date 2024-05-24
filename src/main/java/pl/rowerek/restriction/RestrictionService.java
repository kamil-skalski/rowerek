package pl.rowerek.restriction;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.rowerek.bike.BikeService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.maintenece.MaintenanceService;
import pl.rowerek.reservation.ReservationService;
import pl.rowerek.restriction.dto.CreateRestrictionDto;

import java.time.Instant;

@Service
public class RestrictionService {

    private final RestrictionRepository restrictionRepository;
    private final MaintenanceService maintenanceService;
    private final ReservationService reservationService;
    private final BikeService bikeService;

    public RestrictionService(RestrictionRepository restrictionRepository,
                              @Lazy MaintenanceService maintenanceService,
                              @Lazy ReservationService reservationService, BikeService bikeService) {
        this.restrictionRepository = restrictionRepository;
        this.maintenanceService = maintenanceService;
        this.reservationService = reservationService;
        this.bikeService = bikeService;
    }

    public boolean isInRestriction(BikeId bikeId, Instant currentTime) {
        return restrictionRepository.existsActiveRestrictionByBikeId(bikeId, currentTime);
    }

    public RestrictionId addRestriction(CreateRestrictionDto createDto) {
        bikeService.checkBikeExists(createDto.bikeId());

        if (maintenanceService.existsActiveMaintenanceByBikeId(createDto.bikeId())) {
            throw new BikeIsNotAvailableException("Bike is currently undergoing maintenance: BikeId: " + createDto.bikeId().id());
        }
        if (reservationService.isBikeReserved(createDto.bikeId(), Instant.now())) {
            throw new BikeIsNotAvailableException("Bike is currently reserved: BikeId: " + createDto.bikeId().id());
        }
        var bike = new Restriction(createDto.bikeId(), createDto.reason(), createDto.to());
        return restrictionRepository.save(bike).getId();
    }
}
