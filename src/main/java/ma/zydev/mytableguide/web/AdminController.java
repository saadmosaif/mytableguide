package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.domain.*;
import ma.zydev.mytableguide.repo.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RestaurantRepository restaurantRepo;
    private final DiningTableRepository tableRepo;
    private final ReservationRepository reservationRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    // DASHBOARD
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("restaurantsCount", restaurantRepo.count());
        model.addAttribute("reservationsCount", reservationRepo.count());
        model.addAttribute("usersCount", userRepo.count());
        return "admin/index";
    }

    /* ---------------- RESTAURANTS CRUD ---------------- */

    @GetMapping("/restaurants")
    public String listRestaurants(Model model) {
        model.addAttribute("restaurants", restaurantRepo.findAll());
        model.addAttribute("owners", userRepo.findAll()
                .stream().filter(u -> u.getRole() == User.Role.OWNER).toList());
        return "admin/restaurants";
    }

    @GetMapping("/restaurants/new")
    public String newRestaurant(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("owners", userRepo.findAll()
                .stream().filter(u -> u.getRole() == User.Role.OWNER).toList());
        return "admin/restaurant-form";
    }

    @PostMapping("/restaurants/save")
    public String saveRestaurant(@ModelAttribute Restaurant restaurant,
                                 @RequestParam(value = "owner", required = false) Long ownerId,
                                 RedirectAttributes ra) {

        if (ownerId != null) {
            var owner = userRepo.findById(ownerId).orElse(null);
            restaurant.setOwner(owner);
        } else {
            restaurant.setOwner(null);
        }

        restaurantRepo.save(restaurant);
        ra.addFlashAttribute("msg", "Restaurant saved successfully!");
        return "redirect:/admin/restaurants";
    }


    @GetMapping("/restaurants/edit/{id}")
    public String editRestaurant(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", restaurantRepo.findById(id).orElse(null));
        model.addAttribute("owners", userRepo.findAll()
                .stream().filter(u -> u.getRole() == User.Role.OWNER).toList());
        return "admin/restaurant-form";
    }

    @GetMapping("/restaurants/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        restaurantRepo.deleteById(id);
        return "redirect:/admin/restaurants";
    }

    /* ---------------- TABLES CRUD ---------------- */

    @GetMapping("/restaurants/{restaurantId}/tables")
    public String listTables(@PathVariable Long restaurantId, Model model) {
        var restaurant = restaurantRepo.findById(restaurantId).orElse(null);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tables", tableRepo.findByRestaurantId(restaurantId));
        model.addAttribute("table", new DiningTable());
        return "admin/tables";
    }

    @PostMapping("/restaurants/{restaurantId}/tables/save")
    public String saveTable(@PathVariable Long restaurantId, @ModelAttribute DiningTable table) {
        var restaurant = restaurantRepo.findById(restaurantId).orElse(null);
        table.setRestaurant(restaurant);
        tableRepo.save(table);
        return "redirect:/admin/restaurants/" + restaurantId + "/tables";
    }

    @GetMapping("/restaurants/{restaurantId}/tables/delete/{tableId}")
    public String deleteTable(@PathVariable Long restaurantId, @PathVariable Long tableId) {
        tableRepo.deleteById(tableId);
        return "redirect:/admin/restaurants/" + restaurantId + "/tables";
    }

    /* ---------------- RESERVATIONS CRUD ---------------- */

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationRepo.findAll());
        return "admin/reservations";
    }

    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationRepo.deleteById(id);
        return "redirect:/admin/reservations";
    }

    /* ---------------- USERS (OWNERS) ---------------- */

    @GetMapping("/owners")
    public String listOwners(Model model) {
        model.addAttribute("owners", userRepo.findAll()
                .stream().filter(u -> u.getRole() == User.Role.OWNER).toList());
        return "admin/owners";
    }

    @GetMapping("/owners/new")
    public String newOwner(Model model) {
        model.addAttribute("user", new User());
        return "admin/owner-form";
    }

    @PostMapping("/owners/save")
    public String saveOwner(@ModelAttribute User user) {
        user.setRole(User.Role.OWNER);
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/admin/owners";
    }

    @GetMapping("/owners/delete/{id}")
    public String deleteOwner(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/admin/owners";
    }
}
