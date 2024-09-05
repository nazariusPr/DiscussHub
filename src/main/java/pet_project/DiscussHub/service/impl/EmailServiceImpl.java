package pet_project.DiscussHub.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.service.EmailService;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
  private JavaMailSender mailSender;

  @Override
  public void sendVerificationEmail(String to, String verificationToken) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Email Verification");
    message.setText(verificationToken);

    mailSender.send(message);
  }
}
