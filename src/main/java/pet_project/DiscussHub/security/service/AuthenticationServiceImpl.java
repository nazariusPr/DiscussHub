package pet_project.DiscussHub.security.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.security.service.JwtService;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.mapper.AuthenticationMapper;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.RoleType;
import pet_project.DiscussHub.repository.UserRepository;
import pet_project.DiscussHub.security.AuthenticationService;
import pet_project.DiscussHub.service.RoleService;

import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final RoleService roleService;
  private final AuthenticationMapper authenticationMapper;
  private final AuthenticationManager authenticationManager;

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
  public AuthenticationResponse register(RegisterRequest request) {
    User user = this.authenticationMapper.registerRequestToUser(request);
    user.setRoles(Set.of(this.roleService.getRole(RoleType.ROLE_USER)));
    this.userRepository.save(user);

    String jwtToken = this.jwtService.generateToken(user);
    return new AuthenticationResponse(jwtToken);
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user =
        this.userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "User with email = " + request.getEmail() + " was not found!"));

    String jwtToken = this.jwtService.generateToken(user);
    return new AuthenticationResponse(jwtToken);
  }
}
