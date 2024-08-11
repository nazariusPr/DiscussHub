package pet_project.DiscussHub.controller;

import static pet_project.DiscussHub.constant.AppConstants.USER_LINK;

import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.model.enums.Status;
import pet_project.DiscussHub.service.UserService;

@Slf4j
@RestController
@RequestMapping(USER_LINK)
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
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
  public ResponseEntity<List<UserResponse>> getAll() {
    List<UserResponse> response = this.userService.readAll();

    log.info("** /Getting all users");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(@PathVariable(name = "id") UUID id) {
    UserResponse response = this.userService.readById(id);

    log.info("**/ Getting user by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<UserResponse> getByEmail(@RequestParam(name = "email") String email) {
    UserResponse response = this.userService.readByEmail(email);

    log.info("**/ Getting user by email = " + email);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.hasAccess(#id, authentication) or hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable(name = "id") UUID id,
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
  public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") UUID id) {
    this.userService.delete(id);

    log.info("**/ Deleting user by id = " + id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @MessageMapping("/user.connectUser")
  @SendTo("/user/topic")
  public void connectUser(Principal principal) {
    String email = principal.getName();
    this.userService.updateUserStatus(email, Status.ONLINE);

    log.info("**/ Connect user by email = " + email);
  }

  @MessageMapping("/user.disconnectUser")
  @SendTo("/user/topic")
  public void disconnectUser(Principal principal) {
    String email = principal.getName();
    this.userService.updateUserStatus(email, Status.OFFLINE);

    log.info("**/ Disconnect user by email = " + email);
  }
}
