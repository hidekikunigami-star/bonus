package pe.utec.flyaway.repository;

import pe.utec.flyaway.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
        select b from Booking b
        join fetch b.flight f
        where b.id = :id
        """)
    Optional<Booking> findDetailedById(Long id);

    @Query("""
        select count(b) > 0 from Booking b
        where b.customer.id = :customerId
          and b.flight.departureTime < :newArrival
          and b.flight.arrivalTime > :newDeparture
        """)
    boolean existsOverlappingBooking(Long customerId, LocalDateTime newDeparture, LocalDateTime newArrival);
}
