package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.ReservationRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final RestaurantRepository restaurantRepository;
    private final DiningTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "owner/index";
    }

    @GetMapping("/restaurants/{id}/tables")
    public String tables(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", restaurantRepository.findById(id).orElse(null));
        model.addAttribute("tables", tableRepository.findByRestaurantId(id));
        return "owner/tables";
    }

    @GetMapping("/restaurants/{id}/reservations")
    public String reservations(@PathVariable Long id, Model model) {
        var restaurant = restaurantRepository.findById(id).orElse(null);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservations",
                reservationRepository.findByRestaurantIdAndReservationTimeBetween(
                        id,
                        java.time.LocalDateTime.now().minusYears(5),
                        java.time.LocalDateTime.now().plusYears(5)
                )
        );
        return "owner/reservations";
    }
}
