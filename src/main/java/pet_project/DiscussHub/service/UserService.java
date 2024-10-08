package pet_project.DiscussHub.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.Page.PageDto;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.model.User;

public interface UserService {
  User findUserByEmail(String email);

  UserResponse create(RegisterRequest request);

  List<UserResponse> readAll();

  PageDto<UserResponse> readPage(int page, int size);

  UserResponse readById(UUID id);

  UserResponse readByEmail(String email);

  UserResponse update(UUID id, UserRequest request);

  UserResponse update(String email, UserRequest request);

  void delete(UUID id);

  User updateUserVerifiedStatus(String email, boolean status);

  String uploadUserProfileImage(String email, MultipartFile image);
  void deleteUserProfileImage(String email);
}
