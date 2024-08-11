package pet_project.DiscussHub.service;

import java.util.List;
import java.util.UUID;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.User.UserPageDto;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.Status;

public interface UserService {
  User findUserByEmail(String email);

  UserResponse create(RegisterRequest request);

  List<UserResponse> readAll();

  UserPageDto readPage(int page, int size);

  UserResponse readById(UUID id);

  UserResponse readByEmail(String email);

  UserResponse update(UUID id, UserRequest request);

  UserResponse update(String email, UserRequest request);

  void delete(UUID id);

  void updateUserStatus(String email, Status status);
}
