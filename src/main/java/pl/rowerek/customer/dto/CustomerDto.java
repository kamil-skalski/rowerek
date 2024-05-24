package pl.rowerek.customer.dto;

import java.util.UUID;

public record CustomerDto(UUID id, String firstName, String lastName) {
}
