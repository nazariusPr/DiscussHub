package pet_project.DiscussHub.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_project.DiscussHub.model.EmailVerificationToken;

import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository
    extends JpaRepository<EmailVerificationToken, UUID> {}
