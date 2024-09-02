package pet_project.DiscussHub.security.controller;

import static pet_project.DiscussHub.constant.AppConstants.AUTH_LINK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.security.AuthenticationService;

@Slf4j
@RestController
@RequestMapping(AUTH_LINK)
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Operation(summary = "Register a new user", description = "Registers a new user in the system.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request due to validation errors")
      })
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request, BindingResult result, HttpServletResponse response) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to register new user");
      throw new ValidationException("Bad request");
    }

    log.info("**/ Register new user");
    return ResponseEntity.ok(this.authenticationService.register(request, response));
  }

  @Operation(
      summary = "Authenticate a user",
      description = "Authenticates a user and returns an authentication token.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request due to validation errors")
      })
  @PostMapping("/authentication")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request,
      BindingResult result,
      HttpServletResponse response) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to authenticate user");
      throw new ValidationException("Bad request");
    }

    log.info("**/ Authenticate user");
    return ResponseEntity.ok(this.authenticationService.authenticate(request, response));
  }

  @Operation(
      summary = "Refresh authentication token",
      description = "Refreshes the authentication token using the provided refresh token.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request due to invalid refresh token")
      })
  @GetMapping("/refresh-token")
  public ResponseEntity<AuthenticationResponse> refreshToken(
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    AuthenticationResponse authResponse = this.authenticationService.refreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }
}
