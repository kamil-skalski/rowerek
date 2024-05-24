package pl.rowerek.maintenece;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rowerek.availability.BikeAvailabilityService;
import pl.rowerek.common.BikeId;
import pl.rowerek.common.BikeIsNotAvailableException;
import pl.rowerek.common.BikeNotFoundException;
import pl.rowerek.maintenece.dto.CreateMaintenanceDto;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static pl.rowerek.maintenece.Status.ACTIVE;
import static pl.rowerek.maintenece.Status.FINISHED;

@SpringBootTest
class MaintenanceServiceTest {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private BikeAvailabilityService bikeAvailabilityService;

    @Test
    void cannotAddMaintenanceIfBikeIdNotExists() {
        //given
        var notExistingBikeId = BikeId.newOne();
        var reason = "Naprawie to!";
        var createDto = new CreateMaintenanceDto(notExistingBikeId, reason);

        //expected
        assertThrows(BikeNotFoundException.class, () ->
                maintenanceService.addMaintenance(createDto));
    }

    @Test
    void canAddMaintenanceAndMakeBikeUnavailable() {
        //given
        var bikeId = createAvailableBike();
        var reason = "Naprawie to!";
        var createDto = new CreateMaintenanceDto(bikeId, reason);

        //when
        var maintenanceId = maintenanceService.addMaintenance(createDto);

        //then
        var maintenance = maintenanceRepository.findById(maintenanceId).orElseThrow();
        assertEquals(bikeId, maintenance.getBikeId());
        assertEquals(reason, maintenance.getReason());
        assertEquals(ACTIVE, maintenance.getStatus());
        assertFalse(bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now()));
    }

    @Test
    void cannotAddMaintenanceForUnavailableBike() {
        //given
        var bikeId = createAvailableBike();
        var reason = "Naprawie to!";
        var createDto = new CreateMaintenanceDto(bikeId, reason);
        bikeAvailabilityService.takeBike(bikeId, Instant.now(), Instant.MAX, "Bo tak");

        //expected
        assertThrows(BikeIsNotAvailableException.class, () ->
                maintenanceService.addMaintenance(createDto));
    }

    @Test
    void canFinishMaintenanceAndReleaseBike() {
        //given
        var bikeId = createAvailableBike();
        var reason = "Słaba bateria";
        var createDto = new CreateMaintenanceDto(bikeId, reason);
        var maintenanceId = maintenanceService.addMaintenance(createDto);

        //when
        maintenanceService.finishMaintenance(maintenanceId);

        //then
        var maintenance = maintenanceRepository.findById(maintenanceId).orElseThrow();
        assertEquals(bikeId, maintenance.getBikeId());
        assertEquals(reason, maintenance.getReason());
        assertEquals(FINISHED, maintenance.getStatus());
        assertTrue(bikeAvailabilityService.isAvailabilityBike(bikeId, Instant.now()));
    }

    private BikeId createAvailableBike() {
        var bikeId = BikeId.newOne();
        bikeAvailabilityService.createBikeAvailability(bikeId);
        return bikeId;
    }
}