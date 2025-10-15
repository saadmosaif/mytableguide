package ma.zydev.mytableguide.config;

import ma.zydev.mytableguide.domain.DiningTable;
import ma.zydev.mytableguide.domain.Reservation;
import ma.zydev.mytableguide.domain.Restaurant;
import ma.zydev.mytableguide.domain.User;
import ma.zydev.mytableguide.repo.DiningTableRepository;
import ma.zydev.mytableguide.repo.ReservationRepository;
import ma.zydev.mytableguide.repo.RestaurantRepository;
import ma.zydev.mytableguide.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final DiningTableRepository diningTableRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            RestaurantRepository restaurantRepository,
            DiningTableRepository diningTableRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.restaurantRepository = restaurantRepository;
        this.diningTableRepository = diningTableRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Initialize users regardless of other data
        initializeUsers();

        // Only initialize restaurant data if the database is empty
        if (restaurantRepository.count() == 0) {
            initializeRestaurants();
        }
    }

    private void initializeUsers() {
        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Created admin user");
        }

        // Check if owner user already exists
        if (userRepository.findByUsername("owner").isEmpty()) {
            User owner = User.builder()
                    .username("owner")
                    .password(passwordEncoder.encode("owner123"))
                    .role(User.Role.OWNER)
                    .build();
            userRepository.save(owner);
            System.out.println("Created owner user");
        }
    }

    private void initializeRestaurants() {
        System.out.println("Initializing database with sample data...");

        // Create restaurants
        Restaurant restaurant1 = Restaurant.builder()
                .name("Gourmet Palace")
                .address("123 Main St, City")
                .phone("555-123-4567")
                .email("info@gourmetpalace.com")
                .openingHours("Mon-Sun: 11:00-23:00")
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .name("Seaside Bistro")
                .address("456 Ocean Ave, Beach City")
                .phone("555-987-6543")
                .email("contact@seasidebistro.com")
                .openingHours("Tue-Sun: 12:00-22:00")
                .build();

        Restaurant restaurant3 = Restaurant.builder()
                .name("Urban Diner")
                .address("789 Downtown Blvd, Metro")
                .phone("555-456-7890")
                .email("hello@urbandiner.com")
                .openingHours("Mon-Fri: 7:00-21:00, Sat-Sun: 8:00-22:00")
                .build();

        List<Restaurant> savedRestaurants = restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));

        // Create dining tables for each restaurant
        createTablesForRestaurant(savedRestaurants.get(0));
        createTablesForRestaurant(savedRestaurants.get(1));
        createTablesForRestaurant(savedRestaurants.get(2));

        // Create some sample reservations
        createSampleReservations();

        System.out.println("Database initialization completed successfully!");
    }

    private void createTablesForRestaurant(Restaurant restaurant) {
        // Create tables with different capacities
        DiningTable table1 = DiningTable.builder()
                .label("Table 1")
                .capacity(2)
                .restaurant(restaurant)
                .build();

        DiningTable table2 = DiningTable.builder()
                .label("Table 2")
                .capacity(4)
                .restaurant(restaurant)
                .build();

        DiningTable table3 = DiningTable.builder()
                .label("Table 3")
                .capacity(6)
                .restaurant(restaurant)
                .build();

        DiningTable table4 = DiningTable.builder()
                .label("Table 4")
                .capacity(8)
                .restaurant(restaurant)
                .build();

        diningTableRepository.saveAll(List.of(table1, table2, table3, table4));
    }

    private void createSampleReservations() {
        // Get first restaurant and its tables
        Restaurant restaurant = restaurantRepository.findAll().get(0);
        List<DiningTable> tables = diningTableRepository.findByRestaurantId(restaurant.getId());

        if (!tables.isEmpty()) {
            // Create a few sample reservations
            LocalDateTime now = LocalDateTime.now();

            Reservation reservation1 = Reservation.builder()
                    .clientName("John Doe")
                    .clientEmail("john.doe@example.com")
                    .guests(2)
                    .reservationTime(now.plusDays(1).withHour(19).withMinute(0))
                    .status(Reservation.Status.CONFIRMED)
                    .restaurant(restaurant)
                    .table(tables.get(0))
                    .build();

            Reservation reservation2 = Reservation.builder()
                    .clientName("Jane Smith")
                    .clientEmail("jane.smith@example.com")
                    .guests(4)
                    .reservationTime(now.plusDays(2).withHour(20).withMinute(30))
                    .status(Reservation.Status.PENDING)
                    .restaurant(restaurant)
                    .table(tables.get(1))
                    .build();

            reservationRepository.saveAll(List.of(reservation1, reservation2));
        }
    }
}
