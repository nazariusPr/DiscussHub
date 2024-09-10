package pet_project.DiscussHub.controller;

import static pet_project.DiscussHub.constant.AppConstants.USER_LINK;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.service.UserService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(USER_LINK)
public class UserController {
  private final UserService userService;

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Registers a new user with the provided details.")
  @ApiResponse(responseCode = "201", description = "User created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid input")
  public ResponseEntity<UserResponse> createUser(
      @Validated @RequestBody RegisterRequest request, BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException("Invalid request to create new user");
    }

    UserResponse response = this.userService.create(request);

    log.info("** /Creating new user");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/all")
  @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users")
  @ApiResponse(responseCode = "403", description = "User do not have permission for this action")
  public ResponseEntity<List<UserResponse>> getAll() {
    List<UserResponse> response = this.userService.readAll();

    log.info("** /Getting all users");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves user details by their unique ID.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the user")
  @ApiResponse(responseCode = "404", description = "User not found")
  public ResponseEntity<UserResponse> getById(
      @Parameter(description = "ID of the user to be retrieved") @PathVariable(name = "id")
          UUID id) {
    UserResponse response = this.userService.readById(id);

    log.info("**/ Getting user by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  @Operation(
      summary = "Get user by email",
      description = "Retrieves user details by their email address.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the user")
  @ApiResponse(responseCode = "404", description = "User not found")
  public ResponseEntity<UserResponse> getByEmail(
      @Parameter(description = "Email address of the user to be retrieved")
          @RequestParam(name = "email")
          String email) {
    UserResponse response = this.userService.readByEmail(email);

    log.info("**/ Getting user by email = " + email);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.hasAccess(#id, authentication) or hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  @Operation(
      summary = "Update user by ID",
      description = "Updates user details by their unique ID.")
  @ApiResponse(responseCode = "200", description = "Successfully updated the user")
  @ApiResponse(responseCode = "400", description = "Invalid input")
  @ApiResponse(responseCode = "404", description = "User not found")
  public ResponseEntity<UserResponse> updateUser(
      @Parameter(description = "ID of the user to be updated") @PathVariable(name = "id") UUID id,
      @Validated @RequestBody UserRequest request,
      BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException("Invalid request to update user");
    }

    UserResponse response = this.userService.update(id, request);

    log.info("**/ Updating user by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping
  @Operation(
      summary = "Update current user",
      description = "Updates the details of the currently authenticated user.")
  @ApiResponse(responseCode = "200", description = "Successfully updated the user")
  @ApiResponse(responseCode = "400", description = "Invalid input")
  public ResponseEntity<UserResponse> updateUser(
      @Validated @RequestBody UserRequest request, BindingResult result, Principal principal) {
    if (result.hasErrors()) {
      throw new ValidationException("Invalid request to update user");
    }

    UserResponse response = this.userService.update(principal.getName(), request);

    log.info("**/ Updating user with token = " + principal.getName());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.hasAccess(#id, authentication) or hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique ID.")
  @ApiResponse(responseCode = "204", description = "Successfully deleted the user")
  @ApiResponse(responseCode = "404", description = "User not found")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "ID of the user to be deleted") @PathVariable(name = "id") UUID id) {
    this.userService.delete(id);

    log.info("**/ Deleting user by id = " + id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/upload-image")
  @Operation(
      summary = "Upload a profile image",
      description = "Uploads a new profile image for the user specified by their email address.")
  @ApiResponse(responseCode = "200", description = "Image uploaded successfully")
  @ApiResponse(responseCode = "400", description = "Invalid input")
  @ApiResponse(responseCode = "403", description = "User does not have permission for this action")
  public ResponseEntity<String> uploadProfileImage(
      @RequestParam(name = "image") MultipartFile image, Principal principal) {

    String imageUrl = this.userService.uploadUserProfileImage(principal.getName(), image);

    log.info("**/ Uploaded profile image for user with email = " + principal.getName());
    return ResponseEntity.ok(imageUrl);
  }

  @DeleteMapping("/profile-image")
  @Operation(
      summary = "Delete profile image",
      description = "Deletes the profile image of the currently authenticated user.")
  @ApiResponse(responseCode = "204", description = "Profile image deleted successfully")
  @ApiResponse(responseCode = "403", description = "User does not have permission for this action")
  public ResponseEntity<Void> deleteProfileImage(Principal principal) {
    this.userService.deleteUserProfileImage(principal.getName());

    log.info("**/ Deleted profile image for user with email = " + principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
