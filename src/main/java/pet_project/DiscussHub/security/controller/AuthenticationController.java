package pet_project.DiscussHub.security.controller;

import static pet_project.DiscussHub.constant.AppConstants.AUTH_LINK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pet_project.DiscussHub.dto.Authentication.AuthenticationRequest;
import pet_project.DiscussHub.dto.Authentication.AuthenticationResponse;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.security.service.AuthenticationService;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(AUTH_LINK)
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Operation(summary = "Register a new user", description = "Registers a new user in the system.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request due to validation errors")
      })
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody RegisterRequest request, BindingResult result) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to register new user");
      throw new ValidationException("Bad request");
    }

    this.authenticationService.register(request);

    log.info("**/ Register new user");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(
      summary = "Verify email",
      description = "Verifies a user's email address using the provided verification token.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Email verified successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid or expired verification token"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  @GetMapping
  public ResponseEntity<AuthenticationResponse> verifyEmail(
      @RequestParam UUID token, HttpServletResponse response) {
    log.info("**/ Verify user");
    return ResponseEntity.ok(this.authenticationService.verifyEmail(token, response));
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
  @PostMapping
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
