package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final RestaurantRepository restaurantRepository;
    private final DiningTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    // Home redirects to restaurant list
    @GetMapping("/")
    public String index() {
        return "redirect:/restaurants";
    }

    // List restaurants
    @GetMapping("/restaurants")
    public String restaurants(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "restaurants";
    }

    // List tables for a restaurant
    @GetMapping("/restaurants/{id}/tables")
    public String tables(@PathVariable Long id, Model model) {
        var restaurant = restaurantRepository.findById(id).orElse(null);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tables", tableRepository.findByRestaurantId(id));
        return "tables";
    }

    // List reservations for a restaurant
    @GetMapping("/restaurants/{id}/reservations")
    public String reservations(@PathVariable Long id, Model model) {
        var restaurant = restaurantRepository.findById(id).orElse(null);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservations", reservationRepository.findByRestaurantIdAndReservationTimeBetween(
                id,
                java.time.LocalDateTime.now().minusYears(10),
                java.time.LocalDateTime.now().plusYears(10)
        ));
        return "reservations";
    }
}
