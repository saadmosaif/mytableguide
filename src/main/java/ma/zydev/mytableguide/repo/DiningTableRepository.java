package ma.zydev.mytableguide.repo;
import ma.zydev.mytableguide.domain.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    List<DiningTable> findByRestaurantId(Long restaurantId);
}