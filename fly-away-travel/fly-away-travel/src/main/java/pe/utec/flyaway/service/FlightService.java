package pe.utec.flyaway.service;

import pe.utec.flyaway.domain.Flight;
import pe.utec.flyaway.dto.*;
import pe.utec.flyaway.exception.ApiException;
import pe.utec.flyaway.repository.FlightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flights;

    public FlightService(FlightRepository flights) {
        this.flights = flights;
    }

    public FlightResponse create(FlightCreateRequest req) {
        if (!req.departureTime().isBefore(req.arrivalTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Departure time must be before arrival time");
        }
        if (flights.existsByFlightNumberIgnoreCase(req.flightNumber())) {
            throw new ApiException(HttpStatus.CONFLICT, "Flight number already exists");
        }
        Flight f = new Flight();
        f.setFlightNumber(req.flightNumber());
        f.setAirline(req.airline());
        f.setDepartureTime(req.departureTime());
        f.setArrivalTime(req.arrivalTime());
        f.setAvailableSeats(req.availableSeats());
        return FlightResponse.from(flights.save(f));
    }

    public List<FlightResponse> search(String flightNumber, String airline,
                                       LocalDateTime departureFrom, LocalDateTime departureTo) {
        if (departureFrom != null && departureTo != null && departureFrom.isAfter(departureTo)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid departure date range");
        }
        return flights.search(flightNumber, airline, departureFrom, departureTo)
                .stream().map(FlightResponse::from).toList();
    }

    public Flight findForBooking(Long id) {
        return flights.findByIdForUpdate(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Flight not found"));
    }
}
