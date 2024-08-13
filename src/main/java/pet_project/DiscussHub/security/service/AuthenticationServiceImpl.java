package pet_project.DiscussHub.security.service;

import static pet_project.DiscussHub.constant.AppConstants.AUTH_LINK;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.exception.InvalidTokenException;
import pet_project.DiscussHub.mapper.AuthenticationMapper;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.RoleType;
import pet_project.DiscussHub.repository.UserRepository;
import pet_project.DiscussHub.security.AuthenticationService;
import pet_project.DiscussHub.service.RoleService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final RoleService roleService;
  private final AuthenticationMapper authenticationMapper;
  private final AuthenticationManager authenticationManager;

  @Value("${security.jwt.access-token-expiration}")
  private Long ACCESS_TOKEN_EXPIRATION;

  @Value("${security.jwt.refresh-token-expiration}")
  private Long REFRESH_TOKEN_EXPIRATION;

  @Autowired
  public AuthenticationServiceImpl(
      UserRepository userRepository,
      JwtService jwtService,
      RoleService roleService,
      AuthenticationMapper authenticationMapper,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.roleService = roleService;
    this.authenticationMapper = authenticationMapper;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
    User user = this.authenticationMapper.registerRequestToUser(request);
    user.setRoles(Set.of(this.roleService.getRole(RoleType.ROLE_USER)));
    this.userRepository.save(user);

    String refreshToken = this.getRefreshToken(user);
    this.setRefreshTokenCookie(response, refreshToken);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new AuthenticationResponse(jwtToken);
  }

  @Override
  public AuthenticationResponse authenticate(
      AuthenticationRequest request, HttpServletResponse response) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = this.findUser(request.getEmail());
    String refreshToken = this.getRefreshToken(user);
    this.setRefreshTokenCookie(response, refreshToken);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new AuthenticationResponse(jwtToken);
  }

  @Override
  public AuthenticationResponse refreshToken(String refreshToken) {
    if (refreshToken.isEmpty() || this.jwtService.isTokenExpired(refreshToken)) {
      throw new InvalidTokenException("Refresh token is expired!");
    }
    String email = this.jwtService.extractUsername(refreshToken);
    User user = this.findUser(email);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new AuthenticationResponse(jwtToken);
  }

  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(false);
    refreshTokenCookie.setPath(AUTH_LINK);
    refreshTokenCookie.setMaxAge(Math.toIntExact(this.REFRESH_TOKEN_EXPIRATION / 1000));

    response.addCookie(refreshTokenCookie);
  }

  private String getRefreshToken(User user) {
    return this.jwtService.generateToken(user, this.REFRESH_TOKEN_EXPIRATION);
  }

  private User findUser(String email) {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new EntityNotFoundException("User with email = " + email + " was not found!"));
  }
}
