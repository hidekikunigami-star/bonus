package pe.utec.flyaway.controller;

import pe.utec.flyaway.dto.*;
import pe.utec.flyaway.service.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flights;
    private final BookingService bookings;

    public FlightController(FlightService flights, BookingService bookings) {
        this.flights = flights;
        this.bookings = bookings;
    }

    @PostMapping("/create")
    ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flights.create(request));
    }

    @GetMapping("/search")
    List<FlightResponse> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTo) {
        return flights.search(flightNumber, airline, departureFrom, departureTo);
    }

    @PostMapping("/book")
    ResponseEntity<BookingResponse> book(
            @Valid @RequestBody BookingRequest request,
            org.springframework.security.core.Authentication authentication) {
        Long customerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookings.book(customerId, request.flightId()));
    }
}
