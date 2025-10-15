package ma.zydev.mytableguide.repo;
import ma.zydev.mytableguide.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}