package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.domain.Reservation;
import ma.zydev.mytableguide.domain.DiningTable;
import ma.zydev.mytableguide.repo.ReservationRepository;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final DiningTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    // ✅ 1. Get all reservations for a restaurant
    @GetMapping("/{restaurantId}/reservations")
    public ResponseEntity<?> getReservationsByRestaurant(@PathVariable Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }
        var reservations = reservationRepository
                .findByRestaurantIdAndReservationTimeBetween(
                        restaurantId,
                        LocalDateTime.now().minusYears(10),
                        LocalDateTime.now().plusYears(10))
                .stream()
                .map(ma.zydev.mytableguide.dto.ReservationDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(reservations);
    }


    // ✅ 2. Add a reservation to a restaurant (with a specific table)
    @PostMapping("/{restaurantId}/reservations")
    public ResponseEntity<?> createReservation(@PathVariable Long restaurantId,
                                               @RequestParam Long tableId,
                                               @RequestBody Reservation reservation) {
        var restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) return ResponseEntity.badRequest().body("Restaurant not found");

        var tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isEmpty()) return ResponseEntity.badRequest().body("Table not found");

        var conflicts = reservationRepository.findConflictingReservations(
                tableId, reservation.getReservationTime());
        if (!conflicts.isEmpty()) return ResponseEntity.badRequest().body("Table already reserved at this time!");

        reservation.setRestaurant(restaurantOpt.get());
        reservation.setTable(tableOpt.get());
        reservation.setStatus(Reservation.Status.PENDING);

        var saved = reservationRepository.save(reservation);
        return ResponseEntity.ok(ma.zydev.mytableguide.dto.ReservationDTO.fromEntity(saved));
    }


    @GetMapping("/{restaurantId}/availability")
    public ResponseEntity<?> getAvailableTables(@PathVariable Long restaurantId,
                                                @RequestParam String dateTime) {
        var restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        // ✅ Trim spaces/newlines before parsing
        LocalDateTime requested;
        try {
            requested = LocalDateTime.parse(dateTime.trim());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format! Use YYYY-MM-DDTHH:MM:SS");
        }

        var allTables = tableRepository.findByRestaurantId(restaurantId);

        var available = allTables.stream()
                .filter(t -> reservationRepository
                        .findConflictingReservations(t.getId(), requested)
                        .isEmpty())
                .toList();

        return ResponseEntity.ok(available);
    }




    // ✅ 3. Update reservation status (CONFIRMED or CANCELED)
    @PutMapping("/reservations/{reservationId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long reservationId,
                                          @RequestParam String status) {
        var reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Reservation not found");
        }

        var reservation = reservationOpt.get();
        try {
            reservation.setStatus(Reservation.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: use PENDING, CONFIRMED, or CANCELED");
        }

        var saved = reservationRepository.save(reservation);
        return ResponseEntity.ok(saved);
    }

    // ✅ 4. Delete a reservation
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            return ResponseEntity.badRequest().body("Reservation not found");
        }
        reservationRepository.deleteById(reservationId);
        return ResponseEntity.noContent().build();
    }
}
