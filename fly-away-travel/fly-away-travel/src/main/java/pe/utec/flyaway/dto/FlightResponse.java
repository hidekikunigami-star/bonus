package pe.utec.flyaway.dto;

import pe.utec.flyaway.domain.Flight;
import java.time.LocalDateTime;

public record FlightResponse(Long id, String flightNumber, String airline,
                             LocalDateTime departureTime, LocalDateTime arrivalTime,
                             int availableSeats) {
    public static FlightResponse from(Flight f) {
        return new FlightResponse(f.getId(), f.getFlightNumber(), f.getAirline(),
                f.getDepartureTime(), f.getArrivalTime(), f.getAvailableSeats());
    }
}
