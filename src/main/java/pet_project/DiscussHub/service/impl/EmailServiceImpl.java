package pet_project.DiscussHub.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pet_project.DiscussHub.constant.EmailConstants;
import pet_project.DiscussHub.service.EmailService;
import pet_project.DiscussHub.utils.ClientHelper;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
  private JavaMailSender mailSender;
  private TemplateEngine templateEngine;

  @Override
  public void sendVerificationEmail(String to, String verificationToken) {
    String verificationUrl = ClientHelper.getVerificationUrl(verificationToken);

    Map<String, Object> variables = Map.of("verificationUrl", verificationUrl);

    String emailContent = createEmailTemplate(variables, EmailConstants.VERIFY_EMAIL_TEMPLATE_NAME);
    sendEmail(to, "Email Verification", emailContent);
  }

  private void sendEmail(String to, String subject, String content) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    try {
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content, true);
    } catch (MessagingException e) {
      log.error(e.getMessage());
    }

    mailSender.send(mimeMessage);
  }

  private String createEmailTemplate(Map<String, Object> vars, String templateName) {
    Context context = new Context();
    context.setVariables(vars);

    return templateEngine.process(templateName, context);
  }
}
