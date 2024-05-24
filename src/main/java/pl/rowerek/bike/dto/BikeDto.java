package pl.rowerek.bike.dto;

import pl.rowerek.common.BikeId;

public record BikeDto(BikeId id, String model, String color) {
}
