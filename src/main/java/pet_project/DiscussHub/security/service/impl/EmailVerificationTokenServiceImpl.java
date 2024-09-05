package pet_project.DiscussHub.security.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.exception.TokenExpirationException;
import pet_project.DiscussHub.model.EmailVerificationToken;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.security.repository.EmailVerificationTokenRepository;
import pet_project.DiscussHub.security.service.EmailVerificationTokenService;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {
  private final EmailVerificationTokenRepository repository;

  @Value("${security.verify-token-expiration}")
  private Long expirationTimeInMillis;

  @Override
  public EmailVerificationToken create(User user) {
    EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
    emailVerificationToken.setUser(user);

    LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(expirationTimeInMillis);
    emailVerificationToken.setExpirationTime(expirationTime);

    return this.repository.save(emailVerificationToken);
  }

  @Override
  public String validate(UUID tokenId) {
    EmailVerificationToken verificationToken = this.find(tokenId);
    LocalDateTime expirationTime = verificationToken.getExpirationTime();

    if (expirationTime.isBefore(LocalDateTime.now())) {
      throw new TokenExpirationException("Invalid token");
    }

    String email = verificationToken.getUser().getEmail();
    this.delete(tokenId);

    return email;
  }

  @Override
  public void delete(UUID tokenId) {
    this.find(tokenId);
    this.repository.deleteById(tokenId);
  }

  private EmailVerificationToken find(UUID tokenId) {
    return this.repository
        .findById(tokenId)
        .orElseThrow(() -> new EntityNotFoundException("Invalid token"));
  }
}
