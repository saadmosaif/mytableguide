package ma.zydev.mytableguide.web;

import lombok.RequiredArgsConstructor;
import ma.zydev.mytableguide.domain.DiningTable;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class TableController {

    private final DiningTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    // ✅ Get all tables for a restaurant
    @GetMapping("/{restaurantId}/tables")
    public ResponseEntity<?> getTablesByRestaurant(@PathVariable Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }
        List<DiningTable> tables = tableRepository.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(tables);
    }

    // ✅ Add a new table to a restaurant
    @PostMapping("/{restaurantId}/tables")
    public ResponseEntity<?> addTableToRestaurant(@PathVariable Long restaurantId,
                                                  @RequestBody DiningTable table) {
        var restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        table.setRestaurant(restaurantOpt.get());
        var saved = tableRepository.save(table);
        return ResponseEntity.ok(saved);
    }

    // ✅ Update a table
    @PutMapping("/tables/{tableId}")
    public ResponseEntity<?> updateTable(@PathVariable Long tableId, @RequestBody DiningTable updated) {
        var tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Table not found");
        }

        var existing = tableOpt.get();
        existing.setLabel(updated.getLabel());
        existing.setCapacity(updated.getCapacity());
        var saved = tableRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ✅ Delete a table
    @DeleteMapping("/tables/{tableId}")
    public ResponseEntity<?> deleteTable(@PathVariable Long tableId) {
        if (!tableRepository.existsById(tableId)) {
            return ResponseEntity.badRequest().body("Table not found");
        }
        tableRepository.deleteById(tableId);
        return ResponseEntity.noContent().build();
    }
}
