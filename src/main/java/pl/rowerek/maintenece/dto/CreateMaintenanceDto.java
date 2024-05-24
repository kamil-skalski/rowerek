package pl.rowerek.maintenece.dto;

import pl.rowerek.common.BikeId;

public record CreateMaintenanceDto(BikeId bikeId, String reason) {
}
