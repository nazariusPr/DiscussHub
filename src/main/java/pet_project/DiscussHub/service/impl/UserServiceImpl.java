package pet_project.DiscussHub.service.impl;

import static pet_project.DiscussHub.constant.ErrorConstants.ENTITY_NOT_FOUND_MESSAGE;
import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.Page.PageDto;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.mapper.AuthenticationMapper;
import pet_project.DiscussHub.mapper.UserMapper;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.repository.UserRepository;
import pet_project.DiscussHub.service.ImageService;
import pet_project.DiscussHub.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final ImageService imageService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthenticationMapper authMapper;

  @Override
  public User findUserByEmail(String email) {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, email)));
  }

  @Override
  public UserResponse create(RegisterRequest request) {
    validateRequest(request);

    User user = this.userRepository.save(this.authMapper.registerRequestToUser(request));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public List<UserResponse> readAll() {
    return this.userRepository.findAll().stream()
        .map(this.userMapper::userToUserResponse)
        .collect(Collectors.toList());
  }

  @Override
  public PageDto<UserResponse> readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<User> users = this.userRepository.findAll(pageable);

    return new PageDto<>(
        users.getContent().stream()
            .map(this.userMapper::userToUserResponse)
            .collect(Collectors.toList()),
        users.getNumber(),
        users.getTotalElements(),
        users.getTotalPages());
  }

  @Override
  public UserResponse readById(UUID id) {
    return this.userMapper.userToUserResponse(this.findUser(id));
  }

  @Override
  public UserResponse readByEmail(String email) {
    User user = this.findUserByEmail(email);
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public UserResponse update(UUID id, UserRequest request) {
    validateRequest(request);

    User user =
        this.userRepository.save(this.userMapper.userRequestToUser(request, this.findUser(id)));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public UserResponse update(String email, UserRequest request) {
    validateRequest(request);

    User user =
        this.userRepository.save(
            this.userMapper.userRequestToUser(request, this.findUserByEmail(email)));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public void delete(UUID id) {
    this.userRepository.delete(this.findUser(id));
  }

  @Override
  public User updateUserVerifiedStatus(String email, boolean status) {
    User user = this.findUserByEmail(email);
    user.setVerified(status);

    return this.userRepository.save(user);
  }

  @Override
  public String uploadUserProfileImage(String email, MultipartFile image) {
    User user = this.findUserByEmail(email);
    String currentProfileImage = user.getAvatar();
    this.deleteImage(currentProfileImage);

    String fileName = this.imageService.uploadImage(image);
    user.setAvatar(fileName);

    this.userRepository.save(user);
    return this.imageService.buildImageUrl(fileName);
  }

  @Override
  public void deleteUserProfileImage(String email) {
    User user = this.findUserByEmail(email);
    this.deleteImage(user.getAvatar());

    user.setAvatar(null);
    this.userRepository.save(user);
  }

  private User findUser(UUID id) {
    return this.userRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, id)));
  }

  private void deleteImage(String fileName) {
    if (fileName != null && !fileName.isEmpty()) {
      this.imageService.deleteImage(fileName);
    }
  }
}
