package ma.zydev.mytableguide.repo;

import ma.zydev.mytableguide.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT r FROM Restaurant r WHERE r.owner.username = :username")
    List<Restaurant> findByOwnerUsername(@Param("username") String username);
}
