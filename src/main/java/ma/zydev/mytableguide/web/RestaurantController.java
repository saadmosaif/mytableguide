package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.domain.Restaurant;
import ma.zydev.mytableguide.dto.RestaurantDTO;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    // ✅ GET all restaurants (DTO)
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAll() {
        var list = restaurantRepository.findAll()
                .stream()
                .map(RestaurantDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    // ✅ GET by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(r -> ResponseEntity.ok(RestaurantDTO.fromEntity(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST create
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@RequestBody Restaurant restaurant) {
        var saved = restaurantRepository.save(restaurant);
        return ResponseEntity.ok(RestaurantDTO.fromEntity(saved));
    }

    // ✅ PUT update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        return restaurantRepository.findById(id)
                .map(existing -> {
                    existing.setName(restaurant.getName());
                    existing.setAddress(restaurant.getAddress());
                    existing.setPhone(restaurant.getPhone());
                    existing.setEmail(restaurant.getEmail());
                    existing.setOpeningHours(restaurant.getOpeningHours());
                    var saved = restaurantRepository.save(existing);
                    return ResponseEntity.ok(RestaurantDTO.fromEntity(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!restaurantRepository.existsById(id)) return ResponseEntity.notFound().build();
        restaurantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
