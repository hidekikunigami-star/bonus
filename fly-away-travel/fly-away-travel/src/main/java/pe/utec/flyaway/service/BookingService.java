package pe.utec.flyaway.service;

import pe.utec.flyaway.domain.*;
import pe.utec.flyaway.dto.*;
import pe.utec.flyaway.exception.ApiException;
import pe.utec.flyaway.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final FlightRepository flights;
    private final UserService users;

    public BookingService(BookingRepository bookings, FlightRepository flights, UserService users) {
        this.bookings = bookings;
        this.flights = flights;
        this.users = users;
    }

    @Transactional
    public BookingResponse book(Long customerId, Long flightId) {
        User customer = users.findById(customerId);
        Flight flight = flights.findByIdForUpdate(flightId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Flight not found"));

        LocalDateTime now = LocalDateTime.now();
        if (!flight.getDepartureTime().isAfter(now)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot book a flight that has departed or is in progress");
        }
        if (flight.getAvailableSeats() <= 0) {
            throw new ApiException(HttpStatus.CONFLICT, "No available seats");
        }
        if (bookings.existsOverlappingBooking(customerId, flight.getDepartureTime(), flight.getArrivalTime())) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking conflicts with another flight");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flights.save(flight);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setBookingDate(now);
        Booking saved = bookings.save(booking);

        writeConfirmation(saved);
        return BookingResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public BookingResponse find(Long id) {
        Booking booking = bookings.findDetailedById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        return BookingResponse.from(booking);
    }

    private void writeConfirmation(Booking b) {
        String content = """
                Passenger: %s %s
                Flight: %s
                Departure: %s
                Arrival: %s
                Booking date: %s
                """.formatted(
                b.getCustomer().getFirstName(),
                b.getCustomer().getLastName(),
                b.getFlight().getFlightNumber(),
                b.getFlight().getDepartureTime(),
                b.getFlight().getArrivalTime(),
                b.getBookingDate());

        try {
            Path file = Path.of("flight_booking_email_" + b.getId() + ".txt");
            Files.writeString(file, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create confirmation email");
        }
    }
}
