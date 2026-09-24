package pe.utec.flyaway.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(@NotNull Long flightId) {}
