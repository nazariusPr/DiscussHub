package pet_project.DiscussHub.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByRole(RoleType roleType);
}
