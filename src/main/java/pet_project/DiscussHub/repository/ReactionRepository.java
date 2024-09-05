package pet_project.DiscussHub.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_project.DiscussHub.model.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {}
