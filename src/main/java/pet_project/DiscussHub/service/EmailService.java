package pet_project.DiscussHub.service;

public interface EmailService {
  void sendVerificationEmail(String to, String verificationToken);
}
