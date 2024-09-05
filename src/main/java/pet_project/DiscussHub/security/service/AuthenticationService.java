package pet_project.DiscussHub.security.service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;

@Service
public interface AuthenticationService {
  void register(RegisterRequest request);

  AuthenticationResponse verifyEmail(UUID token, HttpServletResponse response);

  AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response);

  AuthenticationResponse refreshToken(String refreshToken);
}
