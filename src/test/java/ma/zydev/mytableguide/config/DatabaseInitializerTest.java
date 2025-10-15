package ma.zydev.mytableguide.config;

import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.ReservationRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class DatabaseInitializerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testDatabaseInitialization() {
        // Verify restaurants were created
        long restaurantCount = restaurantRepository.count();
        System.out.println("[DEBUG_LOG] Restaurant count: " + restaurantCount);
        assertTrue(restaurantCount >= 3, "At least 3 restaurants should be created");

        // Verify dining tables were created
        long tableCount = diningTableRepository.count();
        System.out.println("[DEBUG_LOG] Table count: " + tableCount);
        assertTrue(tableCount >= 12, "At least 12 tables should be created (4 for each restaurant)");

        // Verify reservations were created
        long reservationCount = reservationRepository.count();
        System.out.println("[DEBUG_LOG] Reservation count: " + reservationCount);
        assertTrue(reservationCount >= 2, "At least 2 reservations should be created");

        // Verify tables are associated with restaurants
        var restaurants = restaurantRepository.findAll();
        for (var restaurant : restaurants) {
            var tables = diningTableRepository.findByRestaurantId(restaurant.getId());
            System.out.println("[DEBUG_LOG] Tables for restaurant " + restaurant.getName() + ": " + tables.size());
            assertTrue(tables.size() >= 4, "Each restaurant should have at least 4 tables");
        }
    }
}