package pe.utec.flyaway.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record FlightCreateRequest(
        @NotBlank @Pattern(regexp = "^[A-Z0-9]{1,6}$") String flightNumber,
        @NotBlank String airline,
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime,
        @NotNull @Min(1) Integer availableSeats
) {}
