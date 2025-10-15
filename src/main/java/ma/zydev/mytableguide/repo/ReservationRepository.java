package ma.zydev.mytableguide.repo;

import ma.zydev.mytableguide.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRestaurantIdAndReservationTimeBetween(
            Long restaurantId,
            LocalDateTime start,
            LocalDateTime end
    );

    // ✅ Custom query to detect conflicts
    @Query("""
           SELECT r FROM Reservation r
           WHERE r.table.id = :tableId
             AND r.status IN ('PENDING', 'CONFIRMED')
             AND ABS(TIMESTAMPDIFF(MINUTE, r.reservationTime, :requestedTime)) < 120
           """)
    List<Reservation> findConflictingReservations(
            @Param("tableId") Long tableId,
            @Param("requestedTime") LocalDateTime requestedTime);
}
