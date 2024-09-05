package pet_project.DiscussHub.security.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;

import java.util.UUID;

@Service
public interface AuthenticationService {
  void register(RegisterRequest request);

  AuthenticationResponse verifyEmail(UUID token, HttpServletResponse response);

  AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response);

  AuthenticationResponse refreshToken(String refreshToken);
}
