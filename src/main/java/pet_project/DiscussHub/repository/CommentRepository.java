package pet_project.DiscussHub.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_project.DiscussHub.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {}
