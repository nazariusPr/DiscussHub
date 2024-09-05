package pet_project.DiscussHub.security.service;

import pet_project.DiscussHub.model.EmailVerificationToken;
import pet_project.DiscussHub.model.User;

import java.util.UUID;

public interface EmailVerificationTokenService {
  EmailVerificationToken create(User user);

  String validate(UUID tokenId);

  void delete(UUID tokenId);
}
