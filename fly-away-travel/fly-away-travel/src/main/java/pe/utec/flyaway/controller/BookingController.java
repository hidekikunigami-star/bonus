package pe.utec.flyaway.controller;

import pe.utec.flyaway.dto.BookingResponse;
import pe.utec.flyaway.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight/book")
public class BookingController {
    private final BookingService bookings;
    public BookingController(BookingService bookings) { this.bookings = bookings; }

    @GetMapping("/{id}")
    BookingResponse get(@PathVariable Long id) {
        return bookings.find(id);
    }
}
