package pl.rowerek.restriction.dto;

import pl.rowerek.common.BikeId;

import java.time.Instant;

public record CreateRestrictionDto(BikeId bikeId, String reason, Instant to) {
}
