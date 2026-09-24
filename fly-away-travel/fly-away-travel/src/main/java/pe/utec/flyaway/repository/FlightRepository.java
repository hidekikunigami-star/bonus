package pe.utec.flyaway.repository;

import pe.utec.flyaway.domain.Flight;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumberIgnoreCase(String flightNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Flight f where f.id = :id")
    Optional<Flight> findByIdForUpdate(@Param("id") Long id);

    @Query("""
        select f from Flight f
        where (:flightNumber is null or lower(f.flightNumber) like lower(concat('%', :flightNumber, '%')))
          and (:airline is null or lower(f.airline) like lower(concat('%', :airline, '%')))
          and (:departureFrom is null or f.departureTime >= :departureFrom)
          and (:departureTo is null or f.departureTime <= :departureTo)
        order by f.departureTime
        """)
    List<Flight> search(
        @Param("flightNumber") String flightNumber,
        @Param("airline") String airline,
        @Param("departureFrom") LocalDateTime departureFrom,
        @Param("departureTo") LocalDateTime departureTo);
}
