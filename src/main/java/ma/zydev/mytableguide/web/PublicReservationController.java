package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.domain.DiningTable;
import ma.zydev.mytableguide.domain.Reservation;
import ma.zydev.mytableguide.domain.Restaurant;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.ReservationRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class PublicReservationController {

    private final RestaurantRepository restaurantRepository;
    private final DiningTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @GetMapping("/{restaurantId}")
    public String showBookingPage(@PathVariable Long restaurantId, Model model) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        List<DiningTable> tables = tableRepository.findByRestaurantId(restaurantId);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tables", tables);
        model.addAttribute("reservation", new Reservation());
        return "client/reserve";
    }

    @PostMapping("/{restaurantId}")
    public String createReservation(@PathVariable Long restaurantId,
                                    @ModelAttribute Reservation reservation,
                                    @RequestParam Long tableId,
                                    RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        DiningTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        // Parse date from form (HTML datetime-local)
        LocalDateTime time = LocalDateTime.parse(reservation.getReservationTime().toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        // Conflict check (reuse your repo logic)
        if (!reservationRepository.findConflictingReservations(tableId, time).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Table unavailable at that time.");
            return "redirect:/reserve/" + restaurantId;
        }

        reservation.setRestaurant(restaurant);
        reservation.setTable(table);
        reservation.setStatus(Reservation.Status.PENDING);
        reservation.setReservationTime(time);

        reservationRepository.save(reservation);
        redirectAttributes.addFlashAttribute("reservation", reservation);
        return "redirect:/reserve/success";
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        if (!model.containsAttribute("reservation")) return "redirect:/";
        return "client/success";
    }
}
