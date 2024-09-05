package pet_project.DiscussHub.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_project.DiscussHub.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Optional<Category> findByName(String name);
}
