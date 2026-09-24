package pe.utec.flyaway.dto;

import pe.utec.flyaway.domain.Booking;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long customerId,
        String firstName,
        String lastName,
        Long flightId,
        String flightNumber,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        LocalDateTime bookingDate
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(),
                b.getCustomer().getFirstName(),
                b.getCustomer().getLastName(),
                b.getFlight().getId(),
                b.getFlight().getFlightNumber(),
                b.getFlight().getDepartureTime(),
                b.getFlight().getArrivalTime(),
                b.getBookingDate());
    }
}
