package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.repo.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final RestaurantRepository restaurantRepo;
    private final DiningTableRepository tableRepo;
    private final ReservationRepository reservationRepo;

    /* ------------- OWNER DASHBOARD ------------- */
    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        var username = auth.getName();
        var restaurants = restaurantRepo.findByOwnerUsername(username);
        model.addAttribute("restaurants", restaurants);
        return "owner/index";
    }

    /* ------------- OWNER: VIEW TABLES ------------- */
    @GetMapping("/restaurants/{id}/tables")
    public String tables(@PathVariable Long id, Authentication auth, Model model) {
        var username = auth.getName();
        var restaurant = restaurantRepo.findById(id)
                .filter(r -> r.getOwner() != null && r.getOwner().getUsername().equals(username))
                .orElse(null);
        if (restaurant == null) return "redirect:/owner"; // unauthorized access

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tables", tableRepo.findByRestaurantId(id));
        return "owner/tables";
    }

    /* ------------- OWNER: VIEW RESERVATIONS ------------- */
    @GetMapping("/restaurants/{id}/reservations")
    public String reservations(@PathVariable Long id, Authentication auth, Model model) {
        var username = auth.getName();
        var restaurant = restaurantRepo.findById(id)
                .filter(r -> r.getOwner() != null && r.getOwner().getUsername().equals(username))
                .orElse(null);
        if (restaurant == null) return "redirect:/owner"; // unauthorized access

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservations",
                reservationRepo.findByRestaurantIdAndReservationTimeBetween(
                        id,
                        java.time.LocalDateTime.now().minusYears(5),
                        java.time.LocalDateTime.now().plusYears(5)
                ));
        return "owner/reservations";
    }
}
